package org.formation.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.*;
import lombok.Data;
import org.formation.service.dto.ProductRequest;

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

	@Transient
	public float total() {
				float total = orderItems.stream().map(i -> i.getPrice() * i.getQuantity()).reduce(0f, (a, b) -> a + b) ;
		 		return total - discount*total;
		 	}

			@Transient
	public List<ProductRequest> getProductRequests() {
				return getOrderItems().stream().map(i -> new ProductRequest(i)).toList();
			}
}
