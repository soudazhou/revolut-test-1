package com.revolut.tests.zkiss.transfersvc.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class Transaction {
    private String id;
    private String accountId;
    private BigDecimal amount;
    private String type; // TODO enum
    private Instant issuedAt;
}
