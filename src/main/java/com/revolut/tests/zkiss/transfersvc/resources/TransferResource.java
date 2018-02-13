package com.revolut.tests.zkiss.transfersvc.resources;


import com.google.common.collect.ImmutableMap;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/transfers")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TransferResource {

    private final DBI dbi;

    public TransferResource(DBI dbi) {
        this.dbi = dbi;
    }

    @POST
    public Object transfer(TransferRequest request) {
        // TODO
        log.info("Transfer req {}", request);
        return ImmutableMap.of("transferred", "false");
    }

}
