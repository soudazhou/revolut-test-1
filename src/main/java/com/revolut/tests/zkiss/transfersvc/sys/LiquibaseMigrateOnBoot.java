package com.revolut.tests.zkiss.transfersvc.sys;

import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
public class LiquibaseMigrateOnBoot implements Managed {
    DataSourceFactory dataSourceFactory;
    Environment environment;
    String changelogFile;

    @Override
    public void start() throws Exception {
        ManagedDataSource ds = dataSourceFactory.build(environment.metrics(), "db");
        Liquibase liquibase = new Liquibase(
                changelogFile,
                new ClassLoaderResourceAccessor(),
                new JdbcConnection(ds.getConnection())
        );
        liquibase.update("");
    }

    @Override
    public void stop() {}
}
