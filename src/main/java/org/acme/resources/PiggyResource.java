package org.acme.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.repositories.PiggyRepository;


/**
 * @author Manoel Rodrigues
 */
@Path("/piggies")
public class PiggyResource {

    @Inject
    PiggyRepository piggyRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response get(@Context SecurityContext context) {
        return piggyRepository.get(context);
    }

    @GET
    @Path("/sync/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    @Transactional
    public Response sync(@Context SecurityContext context,
                         @PathParam("code") String code,
                         @QueryParam("name") String name,
                         @QueryParam("description") String description) {
        return piggyRepository.sync(context, code, name, description);
    }

    @GET
    @Path("/family/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPiggiesByFamilyCode(@PathParam("code") String code) {
        return piggyRepository.getPiggiesByFamilyCode(code);

    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getPiggyById(@Context SecurityContext context,
                                 @PathParam("id") Long id) {
        return piggyRepository.getPiggyById(context, id);

    }

    @GET
    @Path("/{code}/deposit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deposit(@PathParam("code") String code,
                            @QueryParam("value") Double value) {
        return piggyRepository.deposit(code, value);
    }

}
