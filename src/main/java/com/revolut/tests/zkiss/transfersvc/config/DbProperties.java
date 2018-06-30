package com.revolut.tests.zkiss.transfersvc.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DbProperties {

    private String jdbcUrl;

}
