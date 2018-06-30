package com.revolut.tests.zkiss.transfersvc;

import com.revolut.tests.zkiss.transfersvc.config.DbProperties;
import com.revolut.tests.zkiss.transfersvc.handlers.IssueTransactionHandler;
import lombok.extern.slf4j.Slf4j;
import ratpack.func.Action;
import ratpack.guice.Guice;
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
                .props(ClassLoader.getSystemResource("config.properties"))
                .require("/db", DbProperties.class);

        return RatpackServer.of(server -> server
                .registry(Guice.registry(b -> b.module(TransferServiceModule.class)))
                .serverConfig(configureDefault.append(configure))
                .handlers(root -> root
                        .all(RequestLogger.ncsa())
                        .prefix("transfers", transfers -> {
                                    transfers
                                            .post(new IssueTransactionHandler())
                                            .get(":id", ctx -> ctx.render("Return tx " + ctx.getPathTokens().get("id")));
                                }
                        )
                )
        );
    }
}