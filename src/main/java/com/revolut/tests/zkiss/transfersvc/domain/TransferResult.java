package com.revolut.tests.zkiss.transfersvc.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransferResult {
    public static TransferResult fail(String errorCode) {
        return new TransferResult(false, errorCode);
    }

    public static TransferResult success() {
        return new TransferResult(true, null);
    }

    boolean transferred;
    String errorCode;

}
