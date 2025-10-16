package org.formation.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.formation.domain.Order;
import org.formation.service.OrderService;
import org.jboss.resteasy.reactive.RestPath;


@Path("/api/orders")
public class OrderResource {

	@Inject
	OrderService orderService;


	@GET
	@Path("/{orderId}")
	public Order getOrder(@RestPath Long orderId) {
		return orderService.getOrderById(orderId);
	}
	@POST
	public Order createOrder(CreateOrderRequest request) {

		return orderService.createOrder(request);
	}
}
