package org.acme.resources;

import io.quarkus.security.Authenticated;
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
@Path("/user")
public class UserResource {

    @Inject
    UserRepository userRepository;

    @POST
    @Path("/create/child")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createChild(User user) {
        return userRepository.createChild(user);
    }

    @POST
    @Path("/create/parent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createParent(User user) {
        return userRepository.createParent(user);
    }

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response login(@Context SecurityContext securityContext) {
        return userRepository.login(securityContext);
    }

}
