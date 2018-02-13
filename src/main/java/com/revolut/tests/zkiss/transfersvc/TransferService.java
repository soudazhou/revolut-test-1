package com.revolut.tests.zkiss.transfersvc;

import com.revolut.tests.zkiss.transfersvc.config.TransferServiceConfig;
import com.revolut.tests.zkiss.transfersvc.resources.TransferResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class TransferService extends Application<TransferServiceConfig> {
    public static void main(String[] args) throws Exception {
        new TransferService().run(args);
    }

    @Override
    public String getName() {
        return "transfer-service";
    }

    @Override
    public void initialize(Bootstrap<TransferServiceConfig> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<TransferServiceConfig>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TransferServiceConfig configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(TransferServiceConfig transferServiceConfig, Environment environment) throws Exception {
        // TODO
        DBIFactory factory = new DBIFactory();
        DBI dbi = factory.build(environment, transferServiceConfig.getDataSourceFactory(), "db");
        environment.jersey().register(new TransferResource(dbi));

    }
}
