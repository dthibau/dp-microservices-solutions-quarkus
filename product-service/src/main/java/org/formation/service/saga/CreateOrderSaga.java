package org.formation.service.saga;

import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.formation.domain.MaxWeightExceededException;
import org.formation.domain.Ticket;
import org.formation.service.TicketService;


import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.java.Log;

/**
 * Response to CreateOrderSaga commands
 */
@ApplicationScoped
@Slf4j
public class CreateOrderSaga {

	@Inject
	private TicketService ticketService;
	@Channel("order-response")
	private Emitter<KafkaRecord<Long, CommandResponse>> commandResponseEmmiter;



	@Incoming("tickets-command")
	public void handleTicketCommand(TicketCommand ticketCommand) throws JsonProcessingException, MaxWeightExceededException {
		log.info("Receiving command : " + ticketCommand);
		Ticket ticket = null;
		switch (ticketCommand.getCommande()) {
		case "TICKET_CREATE":
			ticket = ticketService.createTicket(ticketCommand.getOrderId(), ticketCommand.getProductRequest());
			break;
		case "TICKET_APPROVE":
			ticket = ticketService.approveTicket(ticketCommand.getOrderId());
			break;
		case "TICKET_REJECT":
			ticket = ticketService.rejectTicket(ticketCommand.getOrderId());
			break;

		}
		commandResponseEmmiter.send(KafkaRecord.of(ticket.getOrderId(),
				new CommandResponse(ticket.getOrderId(), 0, ticketCommand.getCommande())));

	}

}
