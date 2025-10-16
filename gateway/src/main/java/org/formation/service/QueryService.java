package org.formation.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.formation.domain.Livraison;
import org.formation.domain.Order;
import org.formation.domain.OrderDto;

@ApplicationScoped
public class QueryService {

    @Inject
    @RestClient
    DeliveryApi deliveryApi;

    @Inject
    @RestClient
    OrderApi orderApi;

    public Uni<OrderDto> getOrderDetails(Long orderId) {
        Uni<Order> order = orderApi.findOrderByOrderId(orderId);
        Uni<Livraison> livraison = deliveryApi.findLivraisonByOrderId(orderId);

        return Uni.combine().all().unis(order, livraison)
                .with((o, l) -> new OrderDto(o, l));

    }

/*
    @GET
    public Multi<OrderDto> getOrdersDetails() {

    }*/
}
