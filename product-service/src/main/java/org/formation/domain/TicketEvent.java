package org.formation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

    @Enumerated(EnumType.STRING)
    private TicketStatus oldStatus;
    @Enumerated(EnumType.STRING)
	private TicketStatus newStatus;

    private Long ticketId;

    private String ticketPayload;
}
