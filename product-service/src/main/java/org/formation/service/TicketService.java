package org.formation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.formation.domain.ProductRequest;
import org.formation.domain.Ticket;
import org.formation.domain.TicketEvent;
import org.formation.domain.TicketStatus;

import java.util.List;

@ApplicationScoped
@Slf4j
public class TicketService {

    @Inject
    ObjectMapper mapper;

    @Inject
    EntityManager em;              // remplace TicketRepository

    @Inject
    EventService eventService;     // inchangé (bean CDI)

    @Transactional
    public Ticket createTicket(Long orderId, List<ProductRequest> productsRequest) throws JsonProcessingException {
        Ticket t = new Ticket();
        t.setOrderId(String.valueOf(orderId));
        t.setProductRequests(productsRequest);
        t.setStatus(TicketStatus.CREATED);

        em.persist(t);
        em.flush(); // pour disposer de l'id immédiatement si besoin

        TicketEvent event = new TicketEvent(null,null, TicketStatus.CREATED, t.getId(), mapper.writeValueAsString(t));
        em.persist(event);

        log.info("Ticket created: {}", t.getId());
        return t;
    }

    @Transactional
    public Ticket readyToPickUp(Long ticketId) throws JsonProcessingException {
        Ticket t = em.find(Ticket.class, ticketId);
        if (t == null) {
            throw new jakarta.ws.rs.NotFoundException("Ticket not found: " + ticketId);
        }

        TicketEvent event = new TicketEvent(null, t.getStatus(), TicketStatus.READY_TO_PICK, t.getId(), mapper.writeValueAsString(t));
        t.setStatus(TicketStatus.READY_TO_PICK);
        em.persist(event);

        // inutile de save/merge si l'entité est managée; merge au cas où
        em.merge(t);

        log.info("Ticket {} marked READY_TO_PICK", t.getId());
        return t;
    }
}

