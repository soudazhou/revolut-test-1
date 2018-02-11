package com.revolut.tests.zkiss.transfersvc.handlers;

import com.google.common.collect.ImmutableMap;
import com.revolut.tests.zkiss.transfersvc.domain.Transaction;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import static ratpack.jackson.Jackson.fromJson;
import static ratpack.jackson.Jackson.json;

@Slf4j
public class IssueTransactionHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        Promise<TransferRequest> parse = ctx.parse(fromJson(TransferRequest.class));
        ctx.render(parse.map(transferRequest -> {
            Jdbi jdbi = ctx.get(Jdbi.class);
            Handle handle = jdbi.open().begin();
            handle.createQuery("select * from transactions")
                    .mapToBean(Transaction.class)
                    .forEach(tx ->
                            log.info("Found Tx: {}", tx)
                    );
            handle.rollback();

            return json(ImmutableMap.of("result", "Creates new tx"));
        }));
    }
}
