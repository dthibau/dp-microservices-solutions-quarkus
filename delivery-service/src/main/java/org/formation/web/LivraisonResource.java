package org.formation.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.formation.domain.Livraison;
import org.formation.domain.Position;
import org.formation.service.DeliveryService;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;

@Path("/api/livraisons")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LivraisonResource {

	// Exemple si tu as un service :
	@Inject
	DeliveryService deliveryService;

	@GET
	@Path("/unaffected")
	public List<Livraison> getUnaffectedLivraison() {
		return deliveryService.findUnaffected();
	}

	@GET
	@Path("/affected")
	public List<Livraison> getAffectedLivraison() {
		return deliveryService.findAffected();
	}

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

	@GET
	@Path("/orders/{id}")
	public Livraison findByOrderId(@RestPath Long orderId) {
		return deliveryService.findByOrderId(orderId);
	}

	@POST
	@Path("/{livraisonId}/affect/{livreurId}")
	public Livraison affectCourier(@RestPath long livraisonId, @RestPath long livreurId) {
		return deliveryService.affect(livraisonId,livreurId);

	}
}
