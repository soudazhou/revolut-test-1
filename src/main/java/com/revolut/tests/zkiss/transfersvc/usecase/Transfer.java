package com.revolut.tests.zkiss.transfersvc.usecase;

import com.revolut.tests.zkiss.transfersvc.domain.Account;
import com.revolut.tests.zkiss.transfersvc.domain.Transaction;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import com.revolut.tests.zkiss.transfersvc.domain.TransferResult;
import com.revolut.tests.zkiss.transfersvc.persistence.AccountRepo;
import com.revolut.tests.zkiss.transfersvc.persistence.TransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.exceptions.CallbackFailedException;

@Slf4j
public class Transfer {
    private final TransferRequest request;
    private final DBI dbi;

    public Transfer(TransferRequest request, DBI dbi) {
        this.request = request;
        this.dbi = dbi;
    }

    public TransferResult run() {
        try {
            return dbi.inTransaction(((handle, status) -> {
                AccountRepo accountRepo = handle.attach(AccountRepo.class);
                TransactionRepo txRepo = handle.attach(TransactionRepo.class);

                Account from = accountRepo.find(request.getFrom());
                if (from == null) {
                    return TransferResult.fail("from.not-found");
                }
                if (!from.hasAtLeast(request.getAmount())) {
                    return TransferResult.fail("from.insufficient-funds");
                }

                Account to = accountRepo.find(request.getTo());
                if (to == null) {
                    return TransferResult.fail("to.not-found");
                }

                from.debit(request.getAmount());
                to.credit(request.getAmount());

                update(accountRepo, from);
                update(accountRepo, to);
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
            if (e.getCause() instanceof OptimisticLockingFailureException) {
                return TransferResult.fail("optimistic-locking");
            }
            throw e;
        }
    }

    private void update(AccountRepo accountRepo, Account account) {
        int updateCount = accountRepo.updateWithVersion(account);
        if (updateCount == 0) {
            throw new OptimisticLockingFailureException();
        }
    }

    public static class OptimisticLockingFailureException extends RuntimeException {}
}
