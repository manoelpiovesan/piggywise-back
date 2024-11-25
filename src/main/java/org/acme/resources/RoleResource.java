package org.acme.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.acme.entities.Role;
import org.acme.repositories.RoleRepository;


/**
 * @author Manoel Rodrigues
 */
@Path("/roles")
public class RoleResource {

    @Inject
    RoleRepository roleRepository;

    @POST
    @Transactional
    public Response add(Role role) {
        return roleRepository.create(role);
    }

}
