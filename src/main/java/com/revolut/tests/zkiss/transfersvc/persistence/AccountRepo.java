package com.revolut.tests.zkiss.transfersvc.persistence;

import com.revolut.tests.zkiss.transfersvc.domain.Account;

public interface AccountRepo {
    Account find(String sortCode, String accountNumber);
}
