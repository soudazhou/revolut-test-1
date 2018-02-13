package com.revolut.tests.zkiss.transfersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
@JsonDeserialize(builder = Account.AccountBuilder.class)
public class Account {
    @JsonPOJOBuilder(withPrefix = "")
    public static final class AccountBuilder {}

    @NotNull
    String sortCode;
    @NotNull
    String accountNumber;
}