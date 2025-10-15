package org.formation.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.formation.domain.Order;
import org.formation.service.OrderService;


@Path("/api/orders")
public class OrderResource {

	@Inject
	OrderService orderService;

	@POST
	public Order createOrder(CreateOrderRequest request) {

		return orderService.createOrder(request);
	}
}
