package com.revolut.tests.zkiss.transfersvc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ratpack.func.Action;
import ratpack.server.ServerConfigBuilder;
import ratpack.test.embed.EmbeddedApp;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateTransferIT {

    private static final Action<ServerConfigBuilder> EMBEDDED = config -> config
            .development(true)
            .port(0);

    private EmbeddedApp app;

    @Before
    public void setUp() throws Exception {
        app = EmbeddedApp.fromServer(TransferService.server(EMBEDDED));
    }

    @After
    public void tearDown() throws Exception {
        app.close();
    }

    @Test
    public void name() throws Exception {
        app.test(client -> {
            assertThat(client.get("/transfers/asd").getBody().getText()).isEqualTo("Return tx asd");
        });
    }
}