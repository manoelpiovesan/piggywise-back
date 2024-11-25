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
import org.acme.repositories.FamilyRepository;

/**
 * @author Manoel Rodrigues
 */
@Path("/family")
public class FamilyResource {

    @Inject
    FamilyRepository familyRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getFamily(@Context SecurityContext context) {
        return familyRepository.getFamily(context);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    @Authenticated
    public Response getUsers(@Context SecurityContext context) {
        return familyRepository.getMembers(context);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response createFamily(@Context SecurityContext context,
                                 Family family) {
        return familyRepository.create(context, family);
    }

    @POST
    @Path("/join/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response joinFamily(@Context SecurityContext context,
                               @PathParam("code") String code) {
        return familyRepository.join(context, code);
    }

    @GET
    @Path("/leave")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response leaveFamily(@Context SecurityContext context) {
        return familyRepository.leave(context);
    }

}
