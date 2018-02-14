package com.revolut.tests.zkiss.transfersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@Builder
@JsonDeserialize(builder = TransferRequest.TransferRequestBuilder.class)
public final class TransferRequest {
    @JsonPOJOBuilder(withPrefix = "")
    public static final class TransferRequestBuilder {}

    @NotNull
    Account from;

    @NotNull
    Account to;

    @NotNull
    @Min(0)
    BigDecimal amount;

}