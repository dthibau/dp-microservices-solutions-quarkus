package org.formation.domain;

import java.time.LocalDateTime;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;

@Embeddable
@Data
public class DeliveryInformation {

	private LocalDateTime deliveryTime;
	
	@Embedded
	private Address address;
}
