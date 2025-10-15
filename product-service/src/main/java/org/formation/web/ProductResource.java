package org.formation.web;

import java.util.List;

import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.formation.domain.ProductRequest;
import org.formation.domain.Ticket;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;


@Path("/api/tickets")
public class ProductResource {

	@POST
	@ResponseStatus(201)
	@Path("/{orderId}")
	public Ticket acceptOrder(@RestPath Long orderId, List<ProductRequest> productsRequest) {
		return null;
	}

	@PATCH
	@Path("/{ticketId}/pickup")
	public Ticket noteTicketReadyToPickUp(@RestPath Long ticketId) {
		return null;
	}
}
