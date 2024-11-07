package org.acme.repositories;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Task;
import org.acme.entities.User;
import org.acme.enums.TaskStatus;

import java.util.Date;
import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class TaskRepository
        implements PanacheRepository<Task> {

    @Transactional
    public Task create(Task task) {
        task.createdAt = new Date();
        task.updatedAt = new Date();
        task.status = TaskStatus.PENDING;
        persist(task);
        return task;
    }

}
