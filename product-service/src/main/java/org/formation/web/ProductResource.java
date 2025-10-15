package org.formation.web;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.formation.domain.ProductRequest;
import org.formation.domain.Ticket;
import org.formation.service.TicketService;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;


@Path("/api/tickets")
@Slf4j
public class ProductResource {

	@Inject
	TicketService ticketService;

	@GET
	public String getHostInfo() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return "Host: " + addr.getHostName() + " / IP: " + addr.getHostAddress();
		} catch (Exception e) {
			return "Unable to determine host info: " + e.getMessage();
		}
	}

	@POST
	@ResponseStatus(201)
	@Path("/{orderId}")
	public Ticket acceptOrder(@RestPath Long orderId, List<ProductRequest> productsRequest) throws JsonProcessingException {
		Ticket t = ticketService.createTicket(orderId, productsRequest);

		log.info("Ticket created "+ t );

		return t;
	}

	@PATCH
	@Path("/{ticketId}/pickup")
	public Ticket noteTicketReadyToPickUp(@RestPath Long ticketId) throws JsonProcessingException {

		Ticket t = ticketService.readyToPickUp(ticketId);

		log.info("Ticket readyToPickUp "+ t.getId());

		return t;
	}
}
