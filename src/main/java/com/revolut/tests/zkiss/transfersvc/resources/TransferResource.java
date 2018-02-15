package com.revolut.tests.zkiss.transfersvc.resources;


import com.google.common.collect.ImmutableMap;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.skife.jdbi.v2.DBI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/transfers")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TransferResource {

    private final DBI dbi;

    public TransferResource(Jdbi dbi) {
        this.dbi = dbi;
    }

    @POST
    public Object transfer(@Valid @NotNull TransferRequest request) {
        // TODO
        log.info("Transfer req {}", request);
        return ImmutableMap.of("transferred", "false");
    }

}
