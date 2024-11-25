package org.acme.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.acme.entities.Reward;
import org.acme.repositories.RewardRepository;

/**
 * @author Manoel Rodrigues
 */
@Path("/rewards")
public class RewardResource {

    @Inject
    RewardRepository rewardRepository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    public Response add(@Context SecurityContext context, Reward reward,
                        @QueryParam("piggyCode") String piggyCode) {
        return rewardRepository.create(context, reward, piggyCode);
    }

    @GET
    @Path("/{id}/claim")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response claim(@Context SecurityContext context,
                          @PathParam("id") Long id) {
        return rewardRepository.claim(context, id);
    }

}
