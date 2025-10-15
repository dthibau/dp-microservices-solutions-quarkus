package org.formation.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.formation.service.dto.ProductRequest;
import org.formation.service.dto.Ticket;

import java.util.List;

@ApplicationScoped
@Slf4j
public class ProductGateway {

    @Inject
    @RestClient
    ProductService productService;

    // POST /api/tickets/{orderId}
    @Retry(maxRetries = 3, delay = 200) // 3 réessais espacés de 200 ms
    @Timeout(1000)                      // échoue si > 1s
    @CircuitBreaker(
            requestVolumeThreshold = 6,     // fenêtre de 6 requêtes
            failureRatio = 0.5,             // 50% d’échecs => open
            delay = 5000                    // reste open 5s
    )
    @Fallback(fallbackMethod = "acceptOrderFallback")
    public Ticket acceptOrder(Long orderId, List<ProductRequest> items) {
        return productService.acceptOrder(orderId, items);
    }

    // Fallback *signature identique + Exception optionnelle en dernier param*
    public Ticket acceptOrderFallback(Long orderId, List<ProductRequest> items) {
        // à adapter : valeur par défaut, mise en file, message utilisateur, etc.
        log.info("FALLBACK acceptOrder for orderId {}", orderId);
        return null;
    }

    // PATCH /api/tickets/{ticketId}/pickup
    @Retry(maxRetries = 2, delay = 150)
    @Timeout(800)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 3000)
    @Fallback(fallbackMethod = "pickupFallback")
    public Ticket noteTicketReadyToPickUp(Long ticketId) {
        return productService.noteTicketReadyToPickUp(ticketId);
    }

    public Ticket pickupFallback(Long ticketId) {
        log.info("FALLBACK acceptOrder for ticketId {}", ticketId);
        return null;
    }

    // GET /api/tickets (health/ping)
    @Timeout(500)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 2000)
    @Fallback(fallbackMethod = "hostInfoFallback")
    public String getHostInfo() {
        return productService.getHostInfo();
    }

    public String hostInfoFallback() {
        log.info("FALLBACK getHostInfo");
        return "product-service unavailable";
    }
}


