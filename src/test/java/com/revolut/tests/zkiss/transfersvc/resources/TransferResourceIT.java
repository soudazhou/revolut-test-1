package com.revolut.tests.zkiss.transfersvc.resources;

import com.google.common.collect.ImmutableMap;
import com.revolut.tests.zkiss.transfersvc.domain.Account;
import com.revolut.tests.zkiss.transfersvc.domain.TransferRequest;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TransferResourceIT {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TransferResource())
            .build();


    @Test
    public void name() {
        Response post = resources.target("/transfers")
                .request()
                .post(Entity.entity(TransferRequest.builder()
                                .from(Account.builder()
                                        .sortCode("asd")
                                        .accountNumber("asd")
                                        .build())
                                .to(Account.builder()
                                        .sortCode("qwe")
                                        .accountNumber("qwe")
                                        .build())
                                .amount(new BigDecimal("12"))
                                .build(),
                        MediaType.APPLICATION_JSON));

        assertThat(post.readEntity(Map.class)).isEqualTo(ImmutableMap.of("transferred", "false"));
    }
}