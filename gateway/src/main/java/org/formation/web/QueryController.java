package org.formation.web;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.formation.domain.OrderDto;
import org.formation.service.QueryService;
import org.jboss.resteasy.reactive.RestPath;


@Path("/orders")
public class QueryController {

	
		@Inject
		QueryService queryService;
		
		
		@GET
		@Path("/{orderId}")
		public Uni<OrderDto> getOrderDetails(@RestPath long orderId) {
			
			return queryService.getOrderDetails(orderId);
		}
		/*
		@GET
		public Multi<OrderDto> getOrdersDetails() {
			
			return queryService.getOrdersDetails();
		}*/
	
}
