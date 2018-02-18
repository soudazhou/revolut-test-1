package com.revolut.tests.zkiss.transfersvc.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Getter
public class Transaction {
    public static final class TransactionBuilder {

    }

    private final String id;
    private final String accountId;
    private final BigDecimal amount;
    private final TransactionType type;
    private final Instant issuedAt;

    public enum TransactionType {
        IN,
        OUT
    }
}
