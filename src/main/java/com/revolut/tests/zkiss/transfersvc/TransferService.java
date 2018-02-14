package com.revolut.tests.zkiss.transfersvc;

import com.revolut.tests.zkiss.transfersvc.config.TransferServiceConfig;
import com.revolut.tests.zkiss.transfersvc.resources.TransferResource;
import com.revolut.tests.zkiss.transfersvc.sys.LiquibaseMigrateOnBoot;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
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
    public void initialize(Bootstrap<TransferServiceConfig> bootstrap) {}

    @Override
    public void run(TransferServiceConfig transferServiceConfig, Environment environment) throws Exception {
        // TODO
        DBIFactory factory = new DBIFactory();
        DBI dbi = factory.build(environment, transferServiceConfig.getDataSourceFactory(), "dbi");
        environment.jersey().register(new TransferResource(dbi));

        environment.lifecycle().manage(new LiquibaseMigrateOnBoot(
                transferServiceConfig.getDataSourceFactory(),
                environment,
                transferServiceConfig.getLiquibaseChangelog()
        ));
    }

}
