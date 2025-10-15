package org.formation.web;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.formation.domain.Order;


@Path("/api/orders")
public class OrderResource {

	
	@POST
	public Order createOrder(CreateOrderRequest request) {
		
		return null;
	}
}
