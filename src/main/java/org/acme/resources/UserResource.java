package org.acme.resources;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.User;
import org.acme.repositories.RoleRepository;
import org.acme.repositories.UserRepository;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Path("/user")
public class UserResource {

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> list() {
        return userRepository.findAll().list();
    }

    @POST
    @Path("/create/child")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createChild(User user) {

        if (userRepository.find("username", user.username).count() > 0) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        user.password = BcryptUtil.bcryptHash(user.password);
        user.roles.add(roleRepository.find("name", "child").firstResult());
        userRepository.persist(user);

        return Response.ok(user).build();
    }

    @POST
    @Path("/create/parent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createParent(User user) {

        if (userRepository.find("username", user.username).count() > 0) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        user.password = BcryptUtil.bcryptHash(user.password);
        user.roles.add(roleRepository.find("name", "parent").firstResult());
        userRepository.persist(user);

        return Response.ok(user).build();
    }


    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response login(@Context SecurityContext securityContext) {
        User user = userRepository.find("username", securityContext.getUserPrincipal().getName()).firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(user).build();
    }


}
