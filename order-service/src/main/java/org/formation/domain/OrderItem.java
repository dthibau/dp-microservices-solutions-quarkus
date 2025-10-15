package org.formation.domain;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String refProduct;
	
	private float price;
	
	private int quantity;
	
	@ManyToOne
	private Order order;
	
	
}
