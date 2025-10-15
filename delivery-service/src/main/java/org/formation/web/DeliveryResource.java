package com.plb.plbsiapi.delivery;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.formation.web.Position;

@Path("/api/delivery")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeliveryResource {

	// Exemple si tu as un service :
	// @Inject DeliveryService deliveryService;

	@POST
	@Path("/pick/{deliveryId}")
	public Response noteDeliveryPickedUp(@PathParam("deliveryId") long deliveryId) {
		// deliveryService.notePickedUp(deliveryId);
		return Response.noContent().build(); // 204
	}

	@POST
	@Path("/position")
	public Response updatePosition(@Valid Position position) {
		// deliveryService.updatePosition(position);
		return Response.noContent().build(); // 204
	}

	@POST
	@Path("/delivered/{deliveryId}")
	public Response noteDeliveryDelivered(@PathParam("deliveryId") long deliveryId) {
		// deliveryService.noteDelivered(deliveryId);
		return Response.noContent().build(); // 204
	}
}
