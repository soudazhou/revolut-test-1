package com.revolut.tests.zkiss.transfersvc;

import lombok.extern.slf4j.Slf4j;
import ratpack.handling.RequestLogger;
import ratpack.server.RatpackServer;

@Slf4j
public class TransferService {

    public static void main(String... args) throws Exception {
        server().start();
    }

    public static RatpackServer server() throws Exception {
        return RatpackServer.of(server -> server
                .registryOf(registry -> registry.add("World!"))
                .serverConfig(config -> config
                        .yaml(ClassLoader.getSystemResource("config.yaml"))
                )
                .handlers(root -> root
                        .all(RequestLogger.ncsa())
                        .prefix("transfers", transfers -> transfers
                                .post(ctx -> ctx.render("Creates new tx"))
                                .get(":id", ctx -> ctx.render("Return tx " + ctx.getPathTokens().get("id")))
                        )
                )
        );
    }
}