package com.revolut.tests.zkiss.transfersvc.lifecycle;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import ratpack.service.Service;
import ratpack.service.StopEvent;

import javax.inject.Inject;

@Slf4j
public class DataSourceService implements Service {
    private final HikariDataSource dataSource;

    @Inject
    public DataSourceService(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onStop(StopEvent event) throws Exception {
        log.debug("Stopping DataSource");
        dataSource.close();
    }
}
