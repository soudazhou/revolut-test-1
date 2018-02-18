package com.revolut.tests.zkiss.transfersvc.usecase;

import com.revolut.tests.zkiss.transfersvc.domain.Account;
import com.revolut.tests.zkiss.transfersvc.domain.Transaction;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import com.revolut.tests.zkiss.transfersvc.domain.TransferResult;
import com.revolut.tests.zkiss.transfersvc.persistence.AccountRepo;
import com.revolut.tests.zkiss.transfersvc.persistence.TransactionRepo;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;

@Slf4j
public class Transfer {
    public static final String FROM_NOT_FOUND = "from.not-found";
    public static final String FROM_INSUFFICIENT_FUNDS = "from.insufficient-funds";
    public static final String TO_NOT_FOUND = "to.not-found";
    public static final String OPTIMISTIC_LOCKING = "optimistic-locking";

    private final TransferRequest request;
    private final DBI dbi;

    public Transfer(TransferRequest request, DBI dbi) {
        this.request = request;
        this.dbi = dbi;
    }

    public TransferResult run() {
        try {
            return dbi.inTransaction(((handle, status) -> {
                // turns out jdbi throws an exception if setrollbackonly is called explicitly.
                // so we have to rely on throwing exceptions in order to transfer information out of this code block
                // yuck
                AccountRepo accountRepo = handle.attach(AccountRepo.class);

                Account from = accountRepo.find(request.getFrom());
                if (from == null) {
                    throw new TransferFailureException(FROM_NOT_FOUND);
                }
                if (!from.hasAtLeast(request.getAmount())) {
                    throw new TransferFailureException(FROM_INSUFFICIENT_FUNDS);
                }

                Account to = accountRepo.find(request.getTo());
                if (to == null) {
                    throw new TransferFailureException(TO_NOT_FOUND);
                }

                from.debit(request.getAmount());
                to.credit(request.getAmount());

                tryUpdate(accountRepo, from);
                tryUpdate(accountRepo, to);

                TransactionRepo txRepo = handle.attach(TransactionRepo.class);
                txRepo.insert(Transaction.builder()
                        .accountId(from.getId())
                        .type(Transaction.TransactionType.OUT)
                        .amount(request.getAmount())
                        .message(request.getMessage())
                        .build());
                txRepo.insert(Transaction.builder()
                        .accountId(to.getId())
                        .type(Transaction.TransactionType.IN)
                        .amount(request.getAmount())
                        .message(request.getMessage())
                        .build());

                return TransferResult.success();
            }));
        } catch (CallbackFailedException e) {
            if (e.getCause() instanceof TransferFailureException) {
                TransferFailureException fe = (TransferFailureException) e.getCause();
                return TransferResult.fail(fe.errorCode);
            }
            throw e;
        }
    }

    private void tryUpdate(AccountRepo accountRepo, Account account) {
        int updateCount = accountRepo.updateWithVersion(account);
        if (updateCount == 0) {
            throw new TransferFailureException(OPTIMISTIC_LOCKING);
        }
    }

    @Value
    private class TransferFailureException extends RuntimeException {
        String errorCode;
    }
}
