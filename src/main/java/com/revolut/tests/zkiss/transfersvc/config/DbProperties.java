package com.revolut.tests.zkiss.transfersvc.config;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DbProperties {
    private static final String PREFIX = "db";

    public static DbProperties get() {
        return DbProperties.builder()
                .jdbcUrl(ConfigFile.get(PREFIX, "jdbcUrl"))
                .build();
    }

    private String jdbcUrl;

}
