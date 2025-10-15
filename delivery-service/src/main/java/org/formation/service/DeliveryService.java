package org.formation.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.formation.domain.Livraison;
import org.formation.domain.Status;

import java.time.Instant;

@ApplicationScoped
public class DeliveryService {

    @Inject
    EntityManager em;

    @Transactional
    public Livraison createDelivery(Long ticketId) {
        Livraison l = new Livraison();
        l.setCreationDate(Instant.now());
        l.setNoCommande(String.valueOf(ticketId));
        l.setStatus(Status.CREE);

        em.persist(l);
        // Optionnel: em.flush() si tu veux l'ID tout de suite
        return l; // l est managé et portera son id après flush/commit
    }
}
