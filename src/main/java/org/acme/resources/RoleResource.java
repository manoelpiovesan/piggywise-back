package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.acme.entities.Role;
import org.acme.repositories.RoleRepository;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Path("/roles")
public class RoleResource {

    @Inject
    RoleRepository roleRepository;

    @GET
    public List<Role> list() {
        return roleRepository.findAll().list();
    }

    @POST
    @Transactional
    public Response add(Role role) {

        if (roleRepository.find("role", role.role).count() > 0) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        roleRepository.persist(role);
        return Response.ok(role).build();
    }

}
