package com.revolut.tests.zkiss.transfersvc;

import com.codahale.metrics.health.HealthCheck;
import com.revolut.tests.zkiss.transfersvc.config.TransferServiceConfig;
import com.revolut.tests.zkiss.transfersvc.resources.TransferResource;
import io.dropwizard.Application;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
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
        bootstrap.addBundle(new ConfiguredBundle<TransferServiceConfig>() {
            @Override
            public void run(TransferServiceConfig transferServiceConfig, Environment environment) throws Exception {
                ManagedDataSource ds = transferServiceConfig.getDataSourceFactory()
                        .build(environment.metrics(), "db");
                Liquibase liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(), new JdbcConnection(ds.getConnection()));
                liquibase.update("");
            }

            @Override
            public void initialize(Bootstrap<?> bootstrap) {
                // TODO

            }
        });
    }

    @Override
    public void run(TransferServiceConfig transferServiceConfig, Environment environment) throws Exception {
        // TODO
        DBIFactory factory = new DBIFactory();
        DBI dbi = factory.build(environment, transferServiceConfig.getDataSourceFactory(), "db");
        environment.jersey().register(new TransferResource(dbi));

        environment.healthChecks().register("dbcounter", new HealthCheck() {
            @Override
            protected Result check() throws Exception {
                Integer count = dbi.open().createQuery("select count(*) from accounts")
                        .mapTo(Integer.class)
                        .first();
                return count >= 0 ?
                        Result.healthy("Count: %d", count) :
                        Result.unhealthy("");
            }
        });

    }
}
