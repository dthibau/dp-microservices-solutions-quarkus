package org.formation.web;

import java.time.LocalDateTime;
import java.util.List;

import org.formation.domain.*;

import lombok.Data;

@Data
public class CreateOrderRequest {

	  private long restaurantId;
	  private long consumerId;
	  private LocalDateTime deliveryTime;
	  private List<OrderItem> lineItems;
	  private Address deliveryAddress;
	  private PaymentInformation paymentInformation;

	public Order getOrder() {
		Order order = new Order();
		DeliveryInformation df = new DeliveryInformation();
		df.setAddress(deliveryAddress);
		order.setDeliveryInformation(df);
		order.setOrderItems(lineItems);
		order.setPaymentInformation(paymentInformation);

		return order;
	}
}
