package com.revolut.tests.zkiss.transfersvc;

import com.revolut.tests.zkiss.transfersvc.config.TransferServiceConfig;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferServiceIT {

    // no way to run on random port??
    // no way to run WITHOUT port in memory??
    @ClassRule
    public static final DropwizardAppRule<TransferServiceConfig> RULE =
            new DropwizardAppRule<>(TransferService.class,
                    ResourceHelpers.resourceFilePath("config-test.yaml"));

    @Test
    public void appStartsUp() {
        assertThat(RULE.getConfiguration().getDataSourceFactory().getUrl()).isNotNull();

        Response response = RULE.client().target("http://localhost:" + RULE.getLocalPort() + "/").request().get();
        assertThat(response.getStatus()).isEqualTo(404);
    }
}