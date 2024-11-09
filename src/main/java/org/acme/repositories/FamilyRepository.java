package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entities.Family;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class FamilyRepository implements PanacheRepository<Family> {
}
