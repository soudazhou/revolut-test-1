package com.revolut.tests.zkiss.transfersvc.persistence;

import com.revolut.tests.zkiss.transfersvc.domain.Transaction;

public interface TransactionRepo {
    public void insert(Transaction tx);
}
