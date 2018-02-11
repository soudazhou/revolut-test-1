package com.revolut.tests.zkiss.transfersvc.lifecycle;

import com.revolut.tests.zkiss.transfersvc.config.DbProperties;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import ratpack.service.Service;
import ratpack.service.StartEvent;

@Slf4j
public class FlywayService implements Service {

    @Override
    public void onStart(StartEvent event) throws Exception {
        log.info("Migrating db schema");
        Flyway flyway = new Flyway();
        flyway.setDataSource(
                event.getRegistry().get(DbProperties.class).getJdbcUrl(),
                null,
                null
        );
        flyway.migrate();
    }
}
