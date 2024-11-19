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
import org.acme.entities.Task;
import org.acme.entities.User;
import org.acme.enums.TaskStatus;
import org.acme.repositories.FamilyRepository;
import org.acme.repositories.PiggyRepository;
import org.acme.repositories.TaskRepository;
import org.acme.repositories.UserRepository;

import java.util.Date;
import java.util.Objects;

/**
 * @author Manoel Rodrigues
 */
@Path("/tasks")
public class TaskResource {

    @Inject
    TaskRepository taskRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    FamilyRepository familyRepository;

    @Inject
    PiggyRepository piggyRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        return Response.ok(taskRepository.findAll().list()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response create(@Context SecurityContext context, Task task,
                           @QueryParam("piggyId") Long piggyId) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.roles.stream()
                      .noneMatch(role -> Objects.equals(role.name, "parent"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Piggy piggy = piggyRepository.findById(piggyId);

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!Objects.equals(piggy.family.code, user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        task.piggy = piggy;
        task.status = TaskStatus.pending;
        taskRepository.persist(task);

        return Response.ok(task).build();
    }

    @GET
    @Path("/{id}/complete")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response complete(@Context SecurityContext context,
                             @PathParam("id") Long id) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Task task = taskRepository.findById(id);

        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (task.piggy.family.users.stream()
                                   .noneMatch(u -> Objects.equals(u.username,
                                                                  user.username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        task.status = TaskStatus.waiting_approval;
        taskRepository.getEntityManager().merge(task);

        return Response.ok(task).build();
    }

    @GET
    @Path("/{id}/approve")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response approve(@Context SecurityContext context,
                            @PathParam("id") Long id) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.roles.stream()
                      .noneMatch(role -> Objects.equals(role.name, "parent"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Task task = taskRepository.findById(id);

        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!Objects.equals(task.piggy.family.code, user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        task.status = TaskStatus.done;
        taskRepository.getEntityManager().merge(task);

        return Response.ok(task).build();
    }



}
