package org.acme.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Family;
import org.acme.entities.Piggy;
import org.acme.entities.User;
import org.acme.repositories.FamilyRepository;
import org.acme.repositories.PiggyRepository;
import org.acme.repositories.UserRepository;

import java.util.Objects;

/**
 * @author Manoel Rodrigues
 */
@Path("/piggies")
public class PiggyResource {

    @Inject
    PiggyRepository piggyRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    FamilyRepository familyRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response get(@Context SecurityContext context) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(
                piggyRepository.find("FROM Piggy p WHERE p.family.code = ?1",
                                     user.family.code).list()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response add(Piggy piggy) {
        piggyRepository.persist(piggy);
        return Response.ok(piggy).build();
    }

    @GET
    @Path("/sync/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response sync(@Context SecurityContext context,
                         @PathParam("code") String code,
                         @QueryParam("name") String name,
                         @QueryParam("description") String description,
                         @QueryParam("goal") Integer goal) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Piggy piggy =
                piggyRepository.find("FROM Piggy p WHERE p.code = ?1", code)
                               .firstResult();

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        piggy.family = user.family;
        piggy.name = name;
        piggy.description = description;
        piggy.goal = goal;
        piggyRepository.getEntityManager().merge(piggy);
        return Response.ok(piggy).build();
    }

    @GET
    @Path("/family/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPiggiesByFamilyCode(@PathParam("code") String code) {

        Family family = familyRepository.find("code", code).firstResult();

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(
                piggyRepository.find("FROM Piggy p WHERE p.family.code = ?1",
                                     code).list()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getPiggyById(@Context SecurityContext context,
                                 @PathParam("id") Long id) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        Piggy piggy = piggyRepository.findById(id);

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!Objects.equals(piggy.family.code, user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok(piggy).build();

    }

}
