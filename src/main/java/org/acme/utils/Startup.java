package org.acme.utils;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.entities.Piggy;
import org.acme.entities.Role;
import org.acme.entities.User;
import org.acme.repositories.*;

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

    @Inject
    PiggyRepository piggyRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    FamilyRepository familyRepository;

    @Transactional
    void onStart(@Observes StartupEvent ev) {

        List<String> piggiesCodes = List.of("PIGGY1", "PIGGY2", "PIGGY3", "PIGGY4", "PIGGY5");
        piggiesCodes.forEach(code -> {
            if (piggyRepository.find("code", code).count() == 0) {
                Piggy piggy = new Piggy();
                piggy.code = code;
                piggyRepository.persist(piggy);
            }
        });

        List<String> roles = List.of("parent", "child");
        roles.forEach(role -> {
            if (roleRepository.find("name", role).count() == 0) {
                Role newRole = new Role();
                newRole.name = role;
                roleRepository.persist(newRole);
            }
        });

        /// Create Manoel user
        if (userRepository.find("username", "manoel").count() == 0) {
            User user = new User();
            user.username = "manoel";
            user.password = BcryptUtil.bcryptHash("manoel");
            user.name = "Manoel Rodrigues";
            user.roles.add(roleRepository.find("name", "parent").firstResult());
            user.roles.add(roleRepository.find("name", "admin").firstResult());
            user.family = familyRepository.find("code", "DEVPROD").firstResult();
            userRepository.persist(user);
            System.out.println("\n\n\n <<<<<<<<<<<<<<<<<<<< Manoel created! >>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n");
        }


    }

}
