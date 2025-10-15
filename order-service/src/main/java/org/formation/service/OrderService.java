package org.formation.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.formation.domain.Order;
import org.formation.service.dto.ProductRequest;
import org.formation.service.dto.Ticket;
import org.formation.service.saga.CreateOrderSaga;
import org.formation.web.CreateOrderRequest;

import java.util.List;

@ApplicationScoped
@Slf4j
public class OrderService {


    @Inject
    CreateOrderSaga createOrderSaga;
    @Inject
    EntityManager em; // Hibernate ORM (JPA) – pas de Spring Data



    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) {
        // 1) Persist en base locale
        Order order = createOrderRequest.getOrder();
        em.persist(order);
        em.flush(); // pour obtenir l'ID immédiatement

        // 2) Construire la payload pour le product-service
        List<ProductRequest> productRequest = order.getOrderItems()
                .stream()
                .map(ProductRequest::new)
                .toList();

        createOrderSaga.startSaga(order);

        log.info("Saga stated");

        return order;
    }
}
