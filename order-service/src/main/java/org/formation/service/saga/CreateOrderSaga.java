package org.formation.service.saga;

import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.formation.domain.Order;
import org.formation.domain.OrderStatus;


import jakarta.persistence.EntityNotFoundException;
import lombok.extern.java.Log;

@ApplicationScoped
@Slf4j
@Transactional
public class CreateOrderSaga {

	@Channel("tickets-command")
    Emitter<KafkaRecord<Long,TicketCommand>> ticketCommandEventEmitter;

	@Channel("payments-command")
	Emitter<KafkaRecord<Long,PaymentCommand>> paymentCommandEventEmitter;

	@Inject
	EntityManager em;

	
	
	public void startSaga(Order order) {
		log.info("SAGA STARTED for order " + order);

		ticketCommandEventEmitter.send(KafkaRecord.of(order.getId(), new TicketCommand(order.getId(), "TICKET_CREATE", order.getProductRequests())));

	}

	@Incoming("order-response")
	public void handleCommandResponse(CommandResponse commandResponse) {
		try {
		switch (commandResponse.getCommand()) {
		case "TICKET_CREATE":
			handleTicketCreateResponse(commandResponse);
			break;
		case "PAYMENT_AUTHORIZE":
			handlePaymentResponse(commandResponse);
			break;
		}
		} catch (Exception e) {
			log.error("Disgarding Saga Message Response" + e);
		}
	}

	public void handleTicketCreateResponse(CommandResponse ticketResponse) {
		Order order = em.find(Order.class,ticketResponse.getOrderId());
		if (order == null) throw new EntityNotFoundException();


		if (ticketResponse.isOk()) {
			log.info("SAGA Ticket OK  : sending Payment command" + order.getPaymentInformation());
			paymentCommandEventEmitter.send(KafkaRecord.of(order.getId(),
					new PaymentCommand(order.getId(), order.total(), order.getPaymentInformation())));
		} else {
			log.info("SAGA Ticket NOK : rejecting command"  +order.getId());
			// Rejecting order
			order.setStatus(OrderStatus.REJECTED);
		}
	}

	public void handlePaymentResponse(CommandResponse paymentResponse) {


		Order order = em.find(Order.class,paymentResponse.getOrderId());
		if (order == null) throw new EntityNotFoundException();

		if (paymentResponse.isOk()) {
			log.info("SAGA Payment OK : Sending TICKET_APPROVE  APPROVE Command locally " + order.getPaymentInformation());
			ticketCommandEventEmitter.send(KafkaRecord.of(order.getId(),
					new TicketCommand(order.getId(), "TICKET_APPROVE", order.getProductRequests())));
			order.setStatus(OrderStatus.APPROVED);

		} else {
			log.info("SAGA Payment NOK : Sending TICKET_Reject  REJECT Command locally " + order.getPaymentInformation());
			// Rejecting order
			order.setStatus(OrderStatus.REJECTED);
			// Rejacting ticket
			ticketCommandEventEmitter.send(KafkaRecord.of(order.getId(),
					new TicketCommand(order.getId(), "TICKET_REJECT", order.getProductRequests())));
		}
	}
}
