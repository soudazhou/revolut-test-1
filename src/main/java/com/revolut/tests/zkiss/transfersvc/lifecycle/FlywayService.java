package com.revolut.tests.zkiss.transfersvc.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import ratpack.service.Service;
import ratpack.service.StartEvent;

import javax.sql.DataSource;

@Slf4j
public class FlywayService implements Service {

    @Override
    public void onStart(StartEvent event) throws Exception {
        log.info("Migrating db schema");
        Flyway flyway = new Flyway();
        flyway.setDataSource(event.getRegistry().get(DataSource.class));
        flyway.migrate();
    }
}
