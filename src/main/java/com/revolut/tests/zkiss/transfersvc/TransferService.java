package com.revolut.tests.zkiss.transfersvc;

import com.github.arteam.jdbi3.JdbiFactory;
import com.revolut.tests.zkiss.transfersvc.config.TransferServiceConfig;
import com.revolut.tests.zkiss.transfersvc.resources.TransferResource;
import com.revolut.tests.zkiss.transfersvc.sys.LiquibaseMigrateOnBoot;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

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
        JdbiFactory factory = new JdbiFactory();
        Jdbi dbi = factory.build(environment, transferServiceConfig.getDataSourceFactory(), "dbi");
        environment.jersey().register(new TransferResource(dbi));

        environment.lifecycle().manage(new LiquibaseMigrateOnBoot(
                () -> LiquibaseMigrateOnBoot.create(dbi.open(), Handle::getConnection),
                transferServiceConfig.getLiquibaseChangelog()
        ));
    }

}
