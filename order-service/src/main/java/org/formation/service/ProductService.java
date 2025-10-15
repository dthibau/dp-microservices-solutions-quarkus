package org.formation.service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.formation.service.dto.ProductRequest;
import org.formation.service.dto.Ticket;

import java.util.List;

@RegisterRestClient(configKey = "product")
@Path("/api/tickets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ProductService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String getHostInfo();

    // POST /api/tickets/{orderId} 201 -> crée un ticket
    @POST
    @Path("/{orderId}")
    // Si tu veux récupérer explicitement le 201, tu peux retourner Ticket directement :
    Ticket acceptOrder(@PathParam("orderId") Long orderId,
                       List<ProductRequest> productsRequest);

    // PATCH /api/tickets/{ticketId}/pickup -> met à jour le ticket
    @PATCH
    @Path("/{ticketId}/pickup")
    Ticket noteTicketReadyToPickUp(@PathParam("ticketId") Long ticketId);
}
