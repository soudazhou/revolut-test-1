package com.revolut.tests.zkiss.transfersvc.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;

@Data
public class TransferServiceConfig extends Configuration {
    @JsonProperty("db")
    private DataSourceFactory dataSourceFactory;
}
