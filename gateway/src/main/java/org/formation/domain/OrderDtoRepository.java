package org.formation.domain;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class OrderDtoRepository implements PanacheRepository<OrderDto> {

    public Optional<OrderDto> findByOrderId(Long orderId) {
        return find("orderId", orderId).firstResultOptional();
    }
}

