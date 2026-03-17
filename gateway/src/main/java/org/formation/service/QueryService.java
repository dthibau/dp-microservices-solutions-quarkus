package org.formation.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.formation.domain.Livraison;
import org.formation.domain.Order;
import org.formation.domain.OrderDto;
import org.formation.domain.OrderDtoRepository;
import org.formation.service.api.DeliveryApi;
import org.formation.service.api.DeliveryGateway;
import org.formation.service.api.OrderApi;

@ApplicationScoped
public class QueryService {

    @Inject
    @RestClient
    DeliveryApi deliveryApi;

    @Inject
    @RestClient
    OrderApi orderApi;

    @Inject
    OrderDtoRepository orderDtoRepository;

    @Inject
    DeliveryGateway deliveryGateway;

    public OrderDto getOrderDetailsWithDto(Long orderId) {
        return orderDtoRepository.findByOrderId(orderId).orElseThrow();
    }

    public Uni<OrderDto> getOrderDetails(Long orderId) {
        Uni<Order> order = orderApi.findOrderByOrderId(orderId);
        Uni<Livraison> livraison = deliveryGateway.findLivraisonByOrderId(orderId);

        return Uni.combine().all().unis(order, livraison)
                .with((o, l) -> new OrderDto(o, l));

    }

/*
    @GET
    public Multi<OrderDto> getOrdersDetails() {

    }*/
}
