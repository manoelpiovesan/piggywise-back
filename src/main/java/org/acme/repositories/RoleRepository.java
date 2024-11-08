package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entities.Role;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {

}
