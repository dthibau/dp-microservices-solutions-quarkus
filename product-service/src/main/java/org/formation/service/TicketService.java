package org.formation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.formation.domain.*;

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

    public List<Ticket> findAll() {
        return em.createQuery("from Ticket", Ticket.class).getResultList();
    }
    @Transactional
    public Ticket createTicket(Long orderId, List<ProductRequest> productsRequest) throws JsonProcessingException, MaxWeightExceededException {
        ResultDomain resultDomain = Ticket.createTicket(orderId, productsRequest);

        em.persist(resultDomain.getTicket());
        em.persist(resultDomain.getTicketEvent());

        return resultDomain.getTicket();
    }
    @Transactional
	public Ticket approveTicket(Long orderId) throws JsonProcessingException {
        Ticket ticket = em.createQuery(
                        "select t from Ticket t where t.orderId = :orderId", Ticket.class)
                .setParameter("orderId", orderId)   // voir point 3 pour le type
                .getSingleResult();        if (ticket == null)
            throw new EntityNotFoundException("No corresponfing ticket for orderId=" + orderId);
        ResultDomain resultDomain = ticket.approveTicket();

        em.persist(resultDomain.getTicketEvent());

        return resultDomain.getTicket();
    }
    @Transactional
    public Ticket rejectTicket(Long orderId) throws JsonProcessingException {
        Ticket ticket = em.createQuery(
                        "select t from Ticket t where t.orderId = :orderId", Ticket.class)
                .setParameter("orderId", orderId)   // voir point 3 pour le type
                .getSingleResult();
        if (ticket == null) {
            throw new EntityNotFoundException("No corresponding ticket for orderId=" + orderId);
        }
        ResultDomain resultDomain = ticket.rejectTicket();

        em.persist(resultDomain.getTicketEvent());

        return resultDomain.getTicket();
    }
    @Transactional
    public Ticket readyToPickUp(Long ticketId) throws JsonProcessingException {
        Ticket t = em.find(Ticket.class, ticketId);
        if (t == null) {
            throw new jakarta.ws.rs.NotFoundException("Ticket not found: " + ticketId);
        }

        ResultDomain resultDomain = t.readyToPickUp();

        em.persist(resultDomain.getTicketEvent());

        return resultDomain.getTicket();
    }
}

