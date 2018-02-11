package com.revolut.tests.zkiss.transfersvc.handlers;

import com.google.common.collect.ImmutableMap;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import static ratpack.jackson.Jackson.fromJson;
import static ratpack.jackson.Jackson.json;

public class IssueTransactionHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        Promise<TransferRequest> parse = ctx.parse(fromJson(TransferRequest.class));
        ctx.render(parse.map(transferRequest -> json(
                ImmutableMap.of("result", "Creates new tx")
        )));
    }
}
