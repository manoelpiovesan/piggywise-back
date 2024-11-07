package org.acme.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Piggy;
import org.acme.entities.Task;
import org.acme.entities.User;
import org.acme.repositories.PiggyRepository;
import org.acme.repositories.UserRepository;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Path("/piggies")
public class PiggyResource {

    @Inject
    PiggyRepository repository;

    @Inject
    UserRepository userRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public List<Piggy> list(@Context SecurityContext context) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();
        return Piggy.find(
                "SELECT p FROM Piggy p WHERE ?1 MEMBER OF p.members",
                user).list();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/members")
    public List<User> listMembers(Long id) {
        Piggy piggy = repository.findById(id);
        return piggy.members;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/tasks")
    public List<Task> listTasks(Long id) {
        Piggy piggy = repository.findById(id);
        return piggy.tasks;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public List<Piggy> listAll() {
        return Piggy.listAll();
    }

}
