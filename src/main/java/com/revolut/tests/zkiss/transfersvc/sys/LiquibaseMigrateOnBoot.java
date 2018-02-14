package com.revolut.tests.zkiss.transfersvc.sys;

import io.dropwizard.lifecycle.Managed;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

@Slf4j
@Value
public class LiquibaseMigrateOnBoot implements Managed {
    DBI dbi;
    String changelogFile;

    @Override
    public void start() throws Exception {
        try (Handle handle = dbi.open()) {
            Liquibase liquibase = new Liquibase(
                    changelogFile,
                    new ClassLoaderResourceAccessor(),
                    new JdbcConnection(handle.getConnection())
            );
            liquibase.update("");
        }
    }

    @Override
    public void stop() {}
}
