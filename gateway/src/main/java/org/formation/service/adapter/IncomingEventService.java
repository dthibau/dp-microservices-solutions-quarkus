package org.formation.service.adapter;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.formation.domain.OrderDto;
import org.formation.domain.OrderDtoRepository;

import java.util.Optional;

@ApplicationScoped
@Slf4j
public class IncomingEventService {

    @Inject
    OrderDtoRepository orderDtoRepository;

    @Incoming("orders")       // canal "order" => topic channels.order
    @Blocking               // on fait de l'I/O BDD bloquante
    @Transactional
    public void handleOrderEvent(OrderEvent orderEvent) {
        log.info("Received order event: {}", orderEvent);
        if ("ORDER_APPROVED".equals(orderEvent.getStatus())) {
            log.info("Peristing order DTO for approved order: {}", orderEvent.getOrderId());
            OrderDto dto = new OrderDto(orderEvent);
            orderDtoRepository.persist(dto);
        }
    }

    @Incoming("livraisons")    // canal "delivery" => topic channels.delivery
    @Blocking
    @Transactional
    public void handleDeliveryEvent(LivraisonEvent livraisonEvent) {
        log.info("Received delivery event: {}", livraisonEvent);
        if ("LIVREUR_AFFECTE".equals(livraisonEvent.getStatus())) {
            log.info("Updating order DTO with delivery info for order: {}", livraisonEvent.getLivraison().getOrderId());
            var orderId = livraisonEvent.getLivraison().getOrderId();
            Optional<OrderDto> optionalOrderDto = orderDtoRepository.findByOrderId(orderId);
            if (optionalOrderDto.isEmpty() ) {
                    var orderDto = new OrderDto(livraisonEvent);
                    orderDtoRepository.persist(orderDto);
            } else {
                var dto = optionalOrderDto.get();
                var liv = livraisonEvent.getLivraison().getLivreur();
                if (liv != null) {
                    dto.setNomLivreur(liv.getNom());
                    dto.setTelephoneLivreur(liv.getTelephone());
                }
            }


            // entité managée: flush auto en fin de @Transactional
        }
    }
}

