package org.formation.domain;

import java.time.Instant;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.formation.service.adapter.LivraisonEvent;
import org.formation.service.adapter.OrderEvent;

@Data
@NoArgsConstructor
@Entity
public class OrderDto {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	long orderId;
	Instant date;
	private String rue;
	private String ville;
	private String codePostal;
	String nomLivreur;
	String telephoneLivreur;
	
	public OrderDto(Order order, Livraison livraison) {
		this.orderId = order.getId();
		this.date = order.getDate();
		this.rue = order.getDeliveryInformation().getAddress().getRue();
		this.ville = order.getDeliveryInformation().getAddress().getVille();
		this.codePostal = order.getDeliveryInformation().getAddress().getCodePostal();
		this.nomLivreur = livraison.getLivreur().getNom();
		this.telephoneLivreur = livraison.getLivreur().getTelephone();
		
	}
	public OrderDto(OrderEvent orderEvent) {
		this.orderId = orderEvent.getOrderId();
		this.date = orderEvent.getOrder().getDate();
		this.rue = orderEvent.getOrder().getDeliveryInformation().getAddress().getRue();
		this.ville = orderEvent.getOrder().getDeliveryInformation().getAddress().getVille();
		this.codePostal = orderEvent.getOrder().getDeliveryInformation().getAddress().getCodePostal();
	}
	public OrderDto(LivraisonEvent livraisonEvent) {
		this.nomLivreur = livraisonEvent.getLivraison().getLivreur().getNom();
		this.telephoneLivreur = livraisonEvent.getLivraison().getLivreur().getTelephone();
	}
}
