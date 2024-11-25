package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.acme.entities.Role;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {

    public Response create(Role role) {
        if (find("name", role.name).count() > 0) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        persist(role);
        return Response.ok(role).build();
    }
}
