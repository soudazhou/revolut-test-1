package com.revolut.tests.zkiss.transfersvc.usecase;

import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import com.revolut.tests.zkiss.transfersvc.domain.TransferResult;
import com.revolut.tests.zkiss.transfersvc.persistence.AccountRepo;
import com.revolut.tests.zkiss.transfersvc.persistence.TransactionRepo;
import org.skife.jdbi.v2.Handle;

public class Transfer {
    private final TransferRequest request;
    private final Handle handle;

    public Transfer(TransferRequest request, Handle handle) {
        this.request = request;
        this.handle = handle;
    }

    public TransferResult run() {
        AccountRepo accountRepo = handle.attach(AccountRepo.class);
        TransactionRepo txRepo = handle.attach(TransactionRepo.class);

        accountRepo.find(request.getFrom().getSortCode(), request.getFrom().getAccountNumber());

        // TODO
        return null;
    }
}
