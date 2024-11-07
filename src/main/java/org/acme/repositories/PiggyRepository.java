package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entities.Piggy;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class PiggyRepository implements PanacheRepository<Piggy> {

}
