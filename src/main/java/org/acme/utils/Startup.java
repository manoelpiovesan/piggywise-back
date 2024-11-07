package org.acme.utils;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.entities.Task;
import org.acme.entities.User;
import org.acme.enums.ProfileType;
import org.acme.repositories.TaskRepository;
import org.acme.repositories.UserRepository;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class Startup {

    @Inject
    UserRepository userRepository;

    @Inject
    TaskRepository taskRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        System.out.println("The application is starting...");

        List<String> newUsers =
                List.of("manoel", "piggy", "kermit", "gonzo", "fozzy");

        for (String username : newUsers) {
            System.out.println("Creating user: " + username);

            User user = new User();
            user.username = username;
            user.password = BcryptUtil.bcryptHash(username);
            user.profileType = username.equals("manoel") ? ProfileType.PARENT
                                                         : ProfileType.CHILD;

            if (!username.equals("manoel")) {
                user.parents = List.of(userRepository.find("username", "manoel")
                                                     .firstResult());
            }
            user.role = "user";

            User.persist(user);
        }

        List<String> tasks =
                List.of("Task 1", "Task 2", "Task 3", "Task 4", "Task 5");

        for (String taskName : tasks) {
            System.out.println("Creating task: " + taskName);
            Task task = new Task();
            task.title = taskName;
            task.description = "Description of " + taskName;
            task.points = 150;
            task.members = User.listAll();
            taskRepository.create(task);
        }

    }

}
