package com.revolut.tests.zkiss.transfersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

@Value
@Builder
@JsonDeserialize(builder = SortCodeAccountNumber.AccountKeyBuilder.class)
public class SortCodeAccountNumber {
    @JsonPOJOBuilder(withPrefix = "")
    public static final class AccountKeyBuilder {}

    @NotEmpty
    String sortCode;

    @NotEmpty
    String accountNumber;

}