package com.revolut.tests.zkiss.transfersvc;

import com.revolut.tests.zkiss.transfersvc.config.DbProperties;
import com.revolut.tests.zkiss.transfersvc.handlers.IssueTransactionHandler;
import com.revolut.tests.zkiss.transfersvc.lifecycle.FlywayService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
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
                .props(ClassLoader.getSystemResource("config.properties"));

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(DbProperties.get().getJdbcUrl());
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        return RatpackServer.of(server -> server
                .registryOf(registry -> {
                    registry
                            .add(dataSource)
                            .add(Jdbi.create(dataSource)
                                    .installPlugin(new SqlObjectPlugin()))
                            .add(new FlywayService());
                })
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