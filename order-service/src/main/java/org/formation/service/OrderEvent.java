package org.formation.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.formation.domain.Order;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    private long orderId;
    private String status;

    private Order order;
}
