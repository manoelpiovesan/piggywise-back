package org.acme.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Task;
import org.acme.repositories.TaskRepository;

import java.util.Objects;

/**
 * @author Manoel Rodrigues
 */
@Path("/tasks")
public class TaskResource {

    @Inject
    TaskRepository taskRepository;

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
        return taskRepository.create(task, context, piggyId);
    }

    @GET
    @Path("/{id}/complete")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response complete(@Context SecurityContext context,
                             @PathParam("id") Long id) {
        return taskRepository.complete(context, id);
    }

    @GET
    @Path("/{id}/approve")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response approve(@Context SecurityContext context,
                            @PathParam("id") Long id) {
        return taskRepository.approve(context, id);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response delete(@Context SecurityContext context,
                           @PathParam("id") Long id) {
        return taskRepository.delete(context, id);
    }

}
