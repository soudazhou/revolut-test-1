package com.revolut.tests.zkiss.transfersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
@JsonDeserialize(builder = AccountKey.AccountKeyBuilder.class)
public class AccountKey {
    @JsonPOJOBuilder(withPrefix = "")
    public static final class AccountKeyBuilder {}

    @NotNull
    String sortCode;

    @NotNull
    String accountNumber;

}