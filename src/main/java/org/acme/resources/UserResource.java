package org.acme.resources;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entities.Role;
import org.acme.entities.User;
import org.acme.repositories.RoleRepository;
import org.acme.repositories.UserRepository;

import java.util.Date;
import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Path("/users")
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response add(User user) {

        // If the user already exists, return a conflict response
        if (userRepository.find("username", user.username).count() > 0) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        user.password = BcryptUtil.bcryptHash(user.password);
        user.createdAt = new Date();
        userRepository.persist(user);

        return Response.ok(user).build();
    }

}
