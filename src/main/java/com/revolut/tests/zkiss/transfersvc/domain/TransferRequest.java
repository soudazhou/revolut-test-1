package com.revolut.tests.zkiss.transfersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

@Value
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

    @Builder
    private TransferRequest(
            @NonNull Account from,
            @NonNull Account to,
            @NonNull BigDecimal amount
    ) {
        checkArgument(amount.scale() < 2,
                "amount scale needs to be < 2");

        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}