package org.formation.service.dto;

import lombok.Data;
import org.formation.domain.OrderItem;

@Data
public class ProductRequest {

   	private String reference;
    private int quantity;

    public ProductRequest(OrderItem orderItem) {
            this.reference = orderItem.getRefProduct();
            this.quantity = orderItem.getQuantity();
    }
}

