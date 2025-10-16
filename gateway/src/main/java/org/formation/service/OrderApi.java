package org.formation.service;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.formation.domain.Order;
import org.jboss.resteasy.reactive.RestPath;

@RegisterRestClient(configKey="order-api")
@Path("/api/orders")
@Produces("application/json")
@Consumes("application/json")
public interface OrderApi {


    @GET
    @Path("/{orderId}")
    Uni<Order> findOrderByOrderId(@RestPath Long orderId);
}
