package org.formation.service.adapter;

import lombok.Data;
import org.formation.domain.Livraison;
@Data
public class LivraisonEvent {

    private Livraison livraison;

	long orderId;
	private String status;
	public LivraisonEvent() {
		super();
	}

	public LivraisonEvent(Livraison livraison, String status) {
		super();
	this.livraison = livraison;
		this.orderId = livraison.getOrderId();
	}
}
