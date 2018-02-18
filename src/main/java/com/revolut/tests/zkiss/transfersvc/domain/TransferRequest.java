package com.revolut.tests.zkiss.transfersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.revolut.tests.zkiss.transfersvc.validation.Precision;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@Builder
@JsonDeserialize(builder = TransferRequest.TransferRequestBuilder.class)
public final class TransferRequest {
    @JsonPOJOBuilder(withPrefix = "")
    public static final class TransferRequestBuilder {}

    // invariants aren't enforced in order to allow hibernate validator to do it's thing

    @Valid
    @NotNull
    SortCodeAccountNumber from;

    @Valid
    @NotNull
    SortCodeAccountNumber to;

    @NotNull
    @Min(0)
    @Precision(3)
    BigDecimal amount;

    String message;

}