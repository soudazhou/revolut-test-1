package com.revolut.tests.zkiss.transfersvc.usecase;

import com.revolut.tests.zkiss.transfersvc.domain.Account;
import com.revolut.tests.zkiss.transfersvc.domain.Transaction;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import com.revolut.tests.zkiss.transfersvc.domain.TransferResult;
import com.revolut.tests.zkiss.transfersvc.persistence.AccountRepo;
import com.revolut.tests.zkiss.transfersvc.persistence.TransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;

@Slf4j
public class Transfer {
    private final TransferRequest request;
    private final DBI dbi;

    public Transfer(TransferRequest request, DBI dbi) {
        this.request = request;
        this.dbi = dbi;
    }

    public TransferResult run() {
        return dbi.inTransaction(((handle, status) -> {
            AccountRepo accountRepo = handle.attach(AccountRepo.class);
            TransactionRepo txRepo = handle.attach(TransactionRepo.class);

            Account from = accountRepo.find(request.getFrom());
            if (from == null) {
                status.setRollbackOnly();
                return TransferResult.fail("from.not-found");
            }
            if (!from.hasAtLeast(request.getAmount())) {
                status.setRollbackOnly();
                return TransferResult.fail("from.insufficient-funds");
            }

            Account to = accountRepo.find(request.getTo());
            if (to == null) {
                status.setRollbackOnly();
                return TransferResult.fail("to.not-found");
            }

            from.debit(request.getAmount());
            to.credit(request.getAmount());

            if (!tryUpdate(accountRepo, from) ||
                    !tryUpdate(accountRepo, to)) {
                status.setRollbackOnly();
                return TransferResult.fail("optimistic-locking");
            }
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
    }

    private boolean tryUpdate(AccountRepo accountRepo, Account account) {
        int updateCount = accountRepo.updateWithVersion(account);
        return updateCount == 1;
    }
}
