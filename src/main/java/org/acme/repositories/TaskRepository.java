package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Piggy;
import org.acme.entities.Task;
import org.acme.entities.User;
import org.acme.enums.TaskStatus;

import java.util.Objects;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class TaskRepository
        implements PanacheRepository<Task> {

    @Inject
    UserRepository userRepository;

    @Inject
    PiggyRepository piggyRepository;

    public Response create(Task task, SecurityContext context, Long piggyId,
                           Long targetUserId) {
        User user = findUser(context);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.roles.stream()
                      .noneMatch(role -> Objects.equals(role.name, "parent"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Piggy piggy = piggyRepository.findById(piggyId);

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!Objects.equals(piggy.family.code, user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (targetUserId != null) {
            User targetUser = userRepository.findById(targetUserId);
            if (targetUser == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            task.targetUser = targetUser;
        }

        task.piggy = piggy;
        task.status = TaskStatus.pending;
        persist(task);

        return Response.ok(task).build();
    }

    public Response complete(SecurityContext context, Long id) {
        User user = findUser(context);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Task task = findById(id);

        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (task.piggy.family.users.stream()
                                   .noneMatch(u -> Objects.equals(u.username,
                                                                  user.username))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        task.status = TaskStatus.waiting_approval;
        getEntityManager().merge(task);

        return Response.ok(task).build();
    }

    public Response approve(SecurityContext context, Long id) {
        User user = findUser(context);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.roles.stream()
                      .noneMatch(role -> Objects.equals(role.name, "parent"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Task task = findById(id);

        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!Objects.equals(task.piggy.family.code, user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        task.status = TaskStatus.done;
        getEntityManager().merge(task);

        return Response.ok(task).build();
    }

    private User findUser(SecurityContext context) {
        return userRepository.findByUsername(
                context.getUserPrincipal().getName());
    }

    public Response delete(SecurityContext context, Long id) {
        User user = findUser(context);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.roles.stream()
                      .noneMatch(role -> Objects.equals(role.name, "parent"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Task task = findById(id);

        if (task == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!Objects.equals(task.piggy.family.code, user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        delete(task);

        return Response.ok().build();
    }

}
