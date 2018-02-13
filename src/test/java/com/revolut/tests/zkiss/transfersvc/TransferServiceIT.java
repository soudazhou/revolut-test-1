package com.revolut.tests.zkiss.transfersvc;

import com.revolut.tests.zkiss.transfersvc.config.TransferServiceConfig;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferServiceIT {

    @ClassRule
    public static final DropwizardAppRule<TransferServiceConfig> RULE =
            new DropwizardAppRule<TransferServiceConfig>(TransferService.class,
                    ResourceHelpers.resourceFilePath("config.yaml"));

    @Test
    public void appStartsUp() {
        assertThat(RULE.getConfiguration().getDataSourceFactory().getUrl()).isNotNull();

        boolean healthy = RULE.getEnvironment().healthChecks().runHealthCheck("dbcounter").isHealthy();
        assertThat(healthy).isTrue();
    }
}