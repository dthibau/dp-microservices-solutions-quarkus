package org.formation.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.formation.service.ResultDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TicketTest {

    /**
     * Helper pour créer un ProductRequest réel avec sa quantité.
     */
    private ProductRequest pr(int qty) {
        ProductRequest pr = new ProductRequest();
        pr.setQuantity(qty);
        return pr;
    }

    @Test
    @DisplayName("createTicket: total < MAX_WEIGHT → Ticket PENDING avec orderId et lignes")
    void createTicket_underMaxWeight_createsPendingTicket() throws Exception {
        List<ProductRequest> items = List.of(pr(1), pr(1)); // total = 2 (<= 3)

        ResultDomain result = Ticket.createTicket(42L, items);

        assertNotNull(result, "ResultDomain ne doit pas être null");

        Ticket created = (Ticket) result.getTicket();
        assertNotNull(created, "Le Ticket créé doit être présent dans ResultDomain");
        assertEquals(42L, created.getOrderId());
        assertEquals(TicketStatus.PENDING, created.getStatus());
        assertEquals(items, created.getProductRequests());
    }

    @Test
    @DisplayName("createTicket: total == MAX_WEIGHT (borne) → OK")
    void createTicket_equalMaxWeight_isAllowed() throws Exception {
        // MAX_WEIGHT = 3.0f -> intValue()=3
        List<ProductRequest> items = List.of(pr(1), pr(2)); // total = 3

        ResultDomain result = Ticket.createTicket(7L, items);

        Ticket created = (Ticket) result.getTicket();
        assertEquals(TicketStatus.PENDING, created.getStatus());
        assertEquals(7L, created.getOrderId());
    }

    @Test
    @DisplayName("createTicket: total > MAX_WEIGHT → MaxWeightExceededException")
    void createTicket_overMaxWeight_throws() {
        List<ProductRequest> items = List.of(pr(2), pr(2)); // total = 4 (>3)

        assertThrows(MaxWeightExceededException.class,
                () -> Ticket.createTicket(99L, items));
    }

    @Test
    @DisplayName("approveTicket: retourne l'ancien statut et passe à CREATED")
    void approveTicket_transitionsToCreated_withOldStatus() throws Exception {
        Ticket ticket = (Ticket) Ticket.createTicket(1L, List.of(pr(1), pr(1))).getTicket();
        assertEquals(TicketStatus.PENDING, ticket.getStatus());

        ResultDomain result = ticket.approveTicket();

        assertEquals(TicketStatus.CREATED, ticket.getStatus());
        assertEquals(TicketStatus.PENDING, result.getTicketEvent().getOldStatus());
        assertSame(ticket, result.getTicket());
    }

    @Test
    @DisplayName("rejectTicket: retourne l'ancien statut et passe à REJECTED")
    void rejectTicket_transitionsToRejected_withOldStatus() throws Exception {
        Ticket ticket = (Ticket) Ticket.createTicket(1L, List.of(pr(1))).getTicket();
        ticket.setStatus(TicketStatus.PENDING);

        ResultDomain result = ticket.rejectTicket();

        assertEquals(TicketStatus.REJECTED, ticket.getStatus());
        assertEquals(TicketStatus.PENDING, result.getTicketEvent().getOldStatus());
        assertSame(ticket, result.getTicket());
    }

    @Test
    @DisplayName("readyToPickUp: retourne l'ancien statut et passe à READY_TO_PICK")
    void readyToPickUp_transitionsToReady_withOldStatus() throws Exception {
        Ticket ticket = (Ticket) Ticket.createTicket(1L, List.of(pr(1))).getTicket();
        ticket.setStatus(TicketStatus.CREATED); // scénario logique après approve

        ResultDomain result = ticket.readyToPickUp();

        assertEquals(TicketStatus.READY_TO_PICK, ticket.getStatus());
        assertEquals(TicketStatus.CREATED, result.getTicketEvent().getOldStatus());
        assertSame(ticket, result.getTicket());
    }
}

