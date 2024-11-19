package org.acme.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Piggy;
import org.acme.entities.Reward;
import org.acme.entities.User;
import org.acme.enums.RewardStatus;
import org.acme.repositories.PiggyRepository;
import org.acme.repositories.RewardRepository;
import org.acme.repositories.UserRepository;

/**
 * @author Manoel Rodrigues
 */
@Path("/rewards")
public class RewardResource {

    @Inject
    RewardRepository rewardRepository;

    @Inject
    PiggyRepository piggyRepository;

    @Inject
    UserRepository userRepository;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Authenticated
    public Response add(@Context SecurityContext context, Reward reward, @QueryParam("piggyCode") String piggyCode) {
        User user = userRepository.find("username", context.getUserPrincipal().getName()).firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Piggy piggy = piggyRepository.find("code", piggyCode).firstResult();

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!piggy.family.code.equals(user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (user.roles.stream().noneMatch(role -> role.name.equals("parent"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        reward.piggy = piggy;

        rewardRepository.persist(reward);
        return Response.ok(reward).build();
    }

    @GET
    @Path("/{id}/claim")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response claim(@Context SecurityContext context, @PathParam("id") Long id) {
        User user = userRepository.find("username", context.getUserPrincipal().getName()).firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Reward reward = rewardRepository.findById(id);

        if (reward == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!reward.piggy.family.code.equals(user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (!reward.piggy.family.users.contains(user)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (reward.status == RewardStatus.claimed) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        reward.status = RewardStatus.claimed;
        reward.claimedBy = user;
        rewardRepository.persist(reward);

        return Response.ok(reward).build();
    }

}
