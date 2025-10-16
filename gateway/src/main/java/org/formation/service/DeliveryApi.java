package org.formation.service;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.formation.domain.Livraison;

@RegisterRestClient(configKey="delivery-api")
@Path("/api/livraisons")
@Produces("application/json")
@Consumes("application/json")
public interface DeliveryApi {


    @GET
    @Path("/orders/{orderId}")
    Uni<Livraison> findLivraisonByOrderId(Long orderId);
}
