package org.formation.service.saga;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;


@ApplicationScoped
@Slf4j
public class IncomingCommandService {


	@Incoming("payment-command")
	@Outgoing("order-response")
	public CommandResponse handlePayment(PaymentCommand paymentCommand ) {
		log.info("PAYMENT-REQUEST " + paymentCommand.getPaymentInformation());
		CommandResponse paymentResponse;
		
		// Dummy Logic
		if ( paymentCommand.getPaymentInformation().getPaymentToken().startsWith("A") ) {
			paymentResponse = new CommandResponse(paymentCommand.getOrderId(), 0, "PAYMENT_AUTHORIZE");
		} else {
			paymentResponse = new CommandResponse(paymentCommand.getOrderId(), -1, "PAYMENT_AUTHORIZE");
		}
		
		log.info("Sending PAYMENT-RESPONSE : " + paymentResponse.isOk());
		return paymentResponse;
		
	}
}
