package org.formation.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "t_orders") // "order" is a reserved keyword in SQL
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private Instant date;
	
	private float discount;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@Embedded
	private PaymentInformation paymentInformation;
	
	@Embedded
	  private DeliveryInformation deliveryInformation;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	List<OrderItem> orderItems = new ArrayList<>();
	
}
