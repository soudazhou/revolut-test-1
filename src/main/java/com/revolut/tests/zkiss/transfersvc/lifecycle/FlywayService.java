package com.revolut.tests.zkiss.transfersvc.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import ratpack.service.Service;
import ratpack.service.StartEvent;

import javax.inject.Inject;
import javax.sql.DataSource;

@Slf4j
public class FlywayService implements Service {

    private final DataSource dataSource;

    @Inject
    public FlywayService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onStart(StartEvent event) throws Exception {
        log.info("Migrating db schema");
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();
    }
}
