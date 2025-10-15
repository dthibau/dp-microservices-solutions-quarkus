package org.formation.service;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.formation.domain.Livraison;

@ApplicationScoped
@Slf4j
public class ConsumeTicketChannelService {


    @Inject
    DeliveryService deliveryService;

    @Incoming("tickets")      // consomme le canal Kafka "tickets"
    @Blocking                 // bascule sur le worker pool (pas le thread IO)
    @Transactional
    public void receiveTicketEvent(TicketEvent ticketEvent) {
        switch (ticketEvent.getNewStatus()) {

            case "READY_TO_PICK":
                Livraison l = deliveryService.createDelivery(ticketEvent.getTicket().getTicketId());
                log.info("Livraison créée {}",l);
                break;
        }
    }
}
