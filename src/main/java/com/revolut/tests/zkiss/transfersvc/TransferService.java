package com.revolut.tests.zkiss.transfersvc;

import com.revolut.tests.zkiss.transfersvc.config.TransferServiceConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TransferService extends Application<TransferServiceConfig> {
    public static void main(String[] args) {
        new TransferService().run(args);
    }


    @Override
    public void run(TransferServiceConfig transferServiceConfig, Environment environment) throws Exception {
        // TODO

    }
}
