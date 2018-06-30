package com.revolut.tests.zkiss.transfersvc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.revolut.tests.zkiss.transfersvc.config.DbProperties;
import com.revolut.tests.zkiss.transfersvc.lifecycle.DataSourceService;
import com.revolut.tests.zkiss.transfersvc.lifecycle.FlywayService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;

@Slf4j
public class TransferServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataSourceService.class);
        bind(FlywayService.class);
    }

    @Provides
    DataSource dataSource(DbProperties dbProperties) {
        log.debug("Initialising DataSource");
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(dbProperties.getJdbcUrl());
        return new HikariDataSource(hikariConfig);
    }

    @Provides
    Jdbi jdbi(DataSource dataSource) {
        return Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin());
    }

}
