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
import org.acme.entities.User;
import org.acme.projections.FamilyProjection;
import org.acme.repositories.FamilyRepository;
import org.acme.repositories.UserRepository;
import org.acme.utils.RandomIDGenerator;

/**
 * @author Manoel Rodrigues
 */
@Path("/family")
public class FamilyResource {

    @Inject
    UserRepository userRepository;

    @Inject
    FamilyRepository familyRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getFamily(@Context SecurityContext context) {
        User user = userRepository.find("username", context.getUserPrincipal().getName()).firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.family == null) {
            return Response.ok().build();
        }

        FamilyProjection family = familyRepository.find("""
                FROM Family f
                 LEFT JOIN f.users u
                 WHERE f.code = ?1
                 GROUP BY f.id, f.name, f.code, f.description
                """, user.family.code).project(FamilyProjection.class).firstResult();

        return Response.ok(family).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    @Authenticated
    public Response getUsers(@Context SecurityContext context) {
        User user = userRepository.find("username", context.getUserPrincipal().getName()).firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.family == null) {
            return Response.ok().build();
        }

        Family family = familyRepository.find("code", user.family.code).firstResult();

        return Response.ok(family.users).build();
    }




    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response createFamily(@Context SecurityContext context, Family family) {
        User user = userRepository.find("username", context.getUserPrincipal().getName()).firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.roles.stream().noneMatch(role -> role.name.equals("parent"))) {
            return Response.status(Response.Status.FORBIDDEN).entity("User does not have permission to create a family!").build();
        }

        if (user.family != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User already has a family!").build();
        }

        String randomCode = RandomIDGenerator.generate();
        while (familyRepository.find("code", randomCode).count() > 0) {
            randomCode = RandomIDGenerator.generate();
        }

        family.code = randomCode;
        familyRepository.persist(family);

        user.family = family;
        userRepository.persist(user);

        return Response.ok(user.family).build();
    }


    @POST
    @Path("/join/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response joinFamily(@Context SecurityContext context, @PathParam("code") String code) {
        User user = userRepository.find("username", context.getUserPrincipal().getName()).firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.family != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User already has a family!").build();
        }

        Family family = familyRepository.find("code", code).firstResult();

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Family not found!").build();
        }

        user.family = family;
        userRepository.persist(user);

        return Response.ok(family).build();
    }


}
