package org.acme.resources;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Task;
import org.acme.entities.User;
import org.acme.repositories.TaskRepository;
import org.acme.repositories.UserRepository;

import java.util.List;
import java.util.Set;

/**
 * @author Manoel Rodrigues
 */
@Path("/tasks")
public class TaskResource {

    @Inject
    TaskRepository taskRepository;

    @Inject
    UserRepository userRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public List<Task> list() {
        return Task.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public List<Task> list(@Context SecurityContext securityContext) {
        User user = (User) securityContext.getUserPrincipal();
        return Task.find("SELECT t FROM Task t WHERE :user MEMBER OF t.members",
                         "user", user).list();
    }

    @POST
    @Transactional
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Task create(@Context SecurityContext securityContext, Task task) {
        User user = userRepository.find("username",
                                        securityContext.getUserPrincipal()
                                                       .getName())
                                  .firstResult();
        task.members.add(user);
        return taskRepository.create(task);
    }

}
