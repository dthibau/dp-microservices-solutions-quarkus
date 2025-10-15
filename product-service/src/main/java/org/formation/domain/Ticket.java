package org.formation.domain;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	String orderId;
	
	@Enumerated(EnumType.STRING)
	private TicketStatus status;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<ProductRequest> productRequests;
	
	
}
