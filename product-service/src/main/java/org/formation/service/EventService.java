package org.formation.service;

import io.quarkus.scheduler.Scheduled;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.formation.domain.TicketEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
@Slf4j
public class EventService {

    @Channel("tickets")
    Emitter<KafkaRecord<Long, TicketEvent>> emitter;

    @Inject
    EntityManager em;

    @Scheduled(every = "10s") // équiv. fixedDelay=10s
    @Transactional
    void sendEvents() {
        List<TicketEvent> events = em.createQuery("from TicketEvent ").getResultList();
        for (TicketEvent e : events) {
            try {
                log.info("Sending event {}} to topic ticket", e);
                // on envoie avec la clé = ticketId
                emitter.send(KafkaRecord.of(e.getTicketId(), e))
                        .toCompletableFuture()
                        .get(20, TimeUnit.SECONDS);
                em.remove(e);
            } catch (Exception ex) {
                log.warn("Unable to send {}} : {}. Retrying in 10s", e, ex.getMessage());
            }
        }
    }
}
