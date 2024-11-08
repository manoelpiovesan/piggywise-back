package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.entities.Task;
import org.acme.repositories.TaskRepository;

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
    @Transactional
    public Response add(Task task) {
        taskRepository.persist(task);
        return Response.ok(task).build();
    }

}
