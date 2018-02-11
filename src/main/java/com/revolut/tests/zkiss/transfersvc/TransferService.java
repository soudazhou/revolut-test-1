package com.revolut.tests.zkiss.transfersvc;

import lombok.extern.slf4j.Slf4j;
import ratpack.func.Action;
import ratpack.handling.RequestLogger;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfigBuilder;

@Slf4j
public class TransferService {

    public static void main(String... args) throws Exception {
        server().start();
    }

    public static RatpackServer server() throws Exception {
        return server(Action.noop());
    }

    public static RatpackServer server(Action<ServerConfigBuilder> configure) throws Exception {
        Action<ServerConfigBuilder> configureDefault = config -> config
                .yaml(ClassLoader.getSystemResource("config.yaml"));
        return RatpackServer.of(server -> {
                    server
                            .registryOf(registry -> registry.add("World!"))
                            .serverConfig(configureDefault.append(configure))
                            .handlers(root -> root
                                    .all(RequestLogger.ncsa())
                                    .prefix("transfers", transfers -> transfers
                                            .post(ctx -> ctx.render("Creates new tx"))
                                            .get(":id", ctx -> ctx.render("Return tx " + ctx.getPathTokens().get("id")))
                                    )
                            );
                }
        );
    }
}