package org.formation.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PaymentInformation {

	private String paymentToken;
}
