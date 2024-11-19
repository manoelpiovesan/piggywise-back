package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entities.Reward;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class RewardRepository implements PanacheRepository<Reward> {
}
