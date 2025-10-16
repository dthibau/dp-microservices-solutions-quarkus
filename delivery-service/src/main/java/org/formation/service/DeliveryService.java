package org.formation.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.formation.domain.Livraison;
import org.formation.domain.Livreur;
import org.formation.domain.Status;
import org.formation.service.channel.OutgoingEventService;
import org.hibernate.Hibernate;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
@Slf4j
public class DeliveryService {

    @Inject
    EntityManager em;

    @Inject
    OutgoingEventService outgoingEventService;

    @Transactional
    public Livraison createDelivery(Long orderId, Long ticketId) {
        Livraison l = new Livraison();
        l.setCreationDate(Instant.now());
        l.setOrderId(orderId);
        l.setTicketId(ticketId);
        l.setStatus(Status.CREE);

        em.persist(l);
        em.flush();
        outgoingEventService.publishEvent(new LivraisonEvent(l, Status.CREE.toString()));

        return l; // l est managé et portera son id après flush/commit
    }

    @Transactional
    public Livraison affect(Long livraisonId, Long livreurId) {
        log.info("Affecting delivery {} to deliverer {}", livraisonId, livreurId);
        Livraison livraison = em.find(Livraison.class, livraisonId);
        Livreur livreur = em.find(Livreur.class, livreurId);
        livraison.setLivreur(livreur);
        livraison.setStatus(Status.LIVREUR_AFFECTE);

 //       outgoingEventService.publishEvent(new LivraisonEvent(livraison, Status.LIVREUR_AFFECTE.toString()));
log.info("Affected delivery {} to deliverer {}", livraisonId, livreurId);
em.flush();
        return livraison;
    }
    public Livraison findByOrderId(Long orderId) {
        return em.createQuery("SELECT l FROM Livraison l WHERE l.orderId = :orderId", Livraison.class)
                .setParameter("orderId", orderId)
                .getResultList()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No delivery found for orderId: " + orderId));
    }

    public List<Livraison> findUnaffected() {
        return em.createQuery("SELECT l FROM Livraison l WHERE l.livreur IS NULL", Livraison.class)
                .getResultList();
    }

    public List<Livraison> findAffected() {
        return em.createQuery("SELECT l FROM Livraison l WHERE l.livreur IS NOT NULL", Livraison.class)
                .getResultList();
    }
}
