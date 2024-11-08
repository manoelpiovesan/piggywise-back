package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entities.Task;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class TaskRepository
        implements PanacheRepository<Task> {



}
