package com.revolut.tests.zkiss.transfersvc;

import org.junit.Test;
import ratpack.test.embed.EmbeddedApp;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateTransferIT {

    @Test
    public void name() throws Exception {
        try (EmbeddedApp app = EmbeddedApp.fromServer(TransferService.server())) {
            app.test(client -> {
                assertThat(client.get("/transfers/asd").getBody().getText()).isEqualTo("Return tx asd");
            });
        }
    }
}