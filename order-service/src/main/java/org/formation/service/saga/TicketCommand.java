package org.formation.service.saga;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.formation.service.dto.ProductRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCommand {

	Long orderId; 
	
	String commande;
	
	List<ProductRequest> productRequest;
	
}
