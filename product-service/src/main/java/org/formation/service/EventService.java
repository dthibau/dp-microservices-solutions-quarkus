package org.formation.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Value;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class EventService {

    @Channel("tickets")
    Emitter<TicketEvent> ticketEventEmitter;

	public void publish(TicketEvent ticketEvent) {
        ticketEventEmitter.send(ticketEvent);
    }
}
