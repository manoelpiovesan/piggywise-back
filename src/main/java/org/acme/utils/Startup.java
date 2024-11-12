package org.acme.utils;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.entities.Piggy;
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

        // Create Manoel user
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

        List<User> currentUsers = userRepository.listAll();

        for(User user : currentUsers) {
            user.password = BcryptUtil.bcryptHash("manoel");
            System.out.println("<<<<<<<<<<<<<<<<<<<< Trocando senha do usuário: " + user.username + " >>>>>>>>>>>>>>>>>>>>>>>>\n");
            userRepository.persist(user);
        }


    }

}
