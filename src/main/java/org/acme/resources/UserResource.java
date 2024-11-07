package org.acme.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.User;
import org.acme.repositories.UserRepository;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Path("/users")
public class UserResource {

    @Inject
    UserRepository repository;

    @GET
    @Path("/me")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response me(@Context SecurityContext context) {

        System.out.println(context.getUserPrincipal().getName());
        User user = repository.find("username",
                                    context.getUserPrincipal().getName())
                              .firstResult();
        return
                Response.ok(user).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list() {
        return User.listAll();
    }

    @GET
    @Path("/{id}/parents")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listParents(@PathParam("id") Long id) {
        User user = repository.findById(id);
        return user.parents;
    }

    @GET
    @Path("/{id}/children")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listChildren(@PathParam("id") Long id) {
        User user = repository.findById(id);
        return user.children;
    }

}
