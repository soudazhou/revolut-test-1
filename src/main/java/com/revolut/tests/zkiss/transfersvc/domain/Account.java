package com.revolut.tests.zkiss.transfersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = Account.AccountBuilder.class)
public class Account {
    @JsonPOJOBuilder(withPrefix = "")
    public static final class AccountBuilder {}

    @NonNull
    String sortCode;
    @NonNull
    String accountNumber;
}
