package org.formation.service.api;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
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
