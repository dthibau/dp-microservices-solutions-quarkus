package org.formation.service.channel;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.formation.domain.Livraison;
import org.formation.service.LivraisonEvent;

@ApplicationScoped
public class OutgoingEventService {

    @Channel("livraisons")
    Emitter<LivraisonEvent> emitter;

    public void publishEvent(LivraisonEvent livraisonEvent) {

    //    emitter.send(livraisonEvent);
    }
}
