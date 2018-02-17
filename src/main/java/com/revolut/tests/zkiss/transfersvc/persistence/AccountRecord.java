package com.revolut.tests.zkiss.transfersvc.persistence;


import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class AccountRecord {
    private String id;
    private String sortCode;
    private String accountNumber;
    private BigDecimal balance;
    private Instant openedAt;
    private long version;
}
