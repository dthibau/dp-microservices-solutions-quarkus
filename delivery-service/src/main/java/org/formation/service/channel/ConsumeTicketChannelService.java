package org.formation.service.channel;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.formation.domain.Livraison;
import org.formation.service.DeliveryService;
import org.formation.service.TicketEvent;

@ApplicationScoped
@Slf4j
public class ConsumeTicketChannelService {


    @Inject
    DeliveryService deliveryService;

    @Incoming("tickets")      // consomme le canal Kafka "tickets"
    @Blocking                 // bascule sur le worker pool (pas le thread IO)
    @Transactional
    public void receiveTicketEvent(TicketEvent ticketEvent) {
        if (ticketEvent == null || ticketEvent.getNewStatus() == null) {
            log.warn("Received null ticket event");
            return;
        }
        switch (ticketEvent.getNewStatus()) {

            case "READY_TO_PICK":
                Livraison l = deliveryService.createDelivery(ticketEvent.getTicketId(), ticketEvent.getTicketId());
                log.info("Livraison créée {}",l);
                break;
        }
    }
}
