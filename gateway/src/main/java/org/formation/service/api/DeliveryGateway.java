package org.formation.service.api;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.formation.domain.Livraison;
import org.formation.domain.Livreur;

import java.util.List;

@ApplicationScoped
@Slf4j
public class DeliveryGateway {


    @Inject
    @RestClient
    DeliveryApi deliveryApi;

    @Retry(maxRetries = 3, delay = 200) // 3 réessais espacés de 200 ms
    @Timeout(1000)                      // échoue si > 1s
    @CircuitBreaker(
            requestVolumeThreshold = 6,     // fenêtre de 6 requêtes
            failureRatio = 0.5,             // 50% d’échecs => open
            delay = 5000                    // reste open 5s
    )
    @Fallback(fallbackMethod = "findLivraisonFallback")
    public Uni<Livraison> findLivraisonByOrderId(Long orderId) {
        return deliveryApi.findLivraisonByOrderId(orderId);
    }
    public Uni<Livraison> findLivraisonFallback(Long orderId)  {

        log.info("FALLBACK findLivraisonFallback for orderId {}", orderId);
        Livraison livraison = new Livraison();
        livraison.setOrderId(orderId);
        Livreur livreur = new Livreur();
        livraison.setLivreur(livreur);
        return Uni.createFrom().item( livraison);
    }
}
