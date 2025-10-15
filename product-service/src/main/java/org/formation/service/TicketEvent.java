package org.formation.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.formation.domain.Ticket;
import org.formation.domain.TicketStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEvent {

    private TicketStatus oldStatus;
	private TicketStatus newStatus;

    private Ticket ticket;
}
