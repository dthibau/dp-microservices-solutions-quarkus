package org.formation.web;



import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.formation.domain.Livreur;
import org.formation.domain.Position;

@Path("/api/livreurs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LivreurResource {

    @Inject
    EntityManager em;

    @POST
    @Path("/{id}/position")
    @Transactional
    public Response updatePosition(@PathParam("id") long id, Position position) {
        Livreur livreur = em.find(Livreur.class, id);
        if (livreur == null) {
            throw new NotFoundException(); // 404
        }

        livreur.setPosition(position);
        // Panache mettra à jour l’entité à la fin de la transaction

        return Response.accepted().build(); // 202
    }
}

