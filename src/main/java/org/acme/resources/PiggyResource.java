package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entities.Piggy;
import org.acme.repositories.PiggyRepository;

/**
 * @author Manoel Rodrigues
 */
@Path("/piggies")
public class PiggyResource {

    @Inject
    PiggyRepository piggyRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        return Response.ok(piggyRepository.findAll().list()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response add(Piggy piggy) {
        piggyRepository.persist(piggy);
        return Response.ok(piggy).build();
    }

}
