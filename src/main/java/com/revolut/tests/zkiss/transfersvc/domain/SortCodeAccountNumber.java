package com.revolut.tests.zkiss.transfersvc.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

@Value
@Builder
@JsonDeserialize(builder = SortCodeAccountNumber.SortCodeAccountNumberBuilder.class)
public class SortCodeAccountNumber {
    @JsonPOJOBuilder(withPrefix = "")
    public static final class SortCodeAccountNumberBuilder {}

    @NotEmpty
    String sortCode;

    @NotEmpty
    String accountNumber;

}