package org.acme.utils;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.entities.Family;
import org.acme.entities.Piggy;
import org.acme.entities.Role;
import org.acme.entities.User;
import org.acme.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class Startup {

    private static final Logger log = LoggerFactory.getLogger(Startup.class);
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

        if(piggyRepository.count() < 2){
            for (int i = 0; i < 100; i++) {
                Piggy piggy = new Piggy();
                piggy.code = "PIGGY" + i;
                piggy.name = "Piggy " + i;
                piggy.description = "Piggy " + i + " description";
                piggyRepository.persist(piggy);
            }
        }

        // Creating roles
        List<String> roles = List.of("parent", "child");
        roles.forEach(role -> {
            if (roleRepository.find("name", role).count() == 0) {
                Role newRole = new Role();
                newRole.name = role;
                roleRepository.persist(newRole);
            }
        });

        // Creating DEVPRO family
        if (familyRepository.find("code", "DEVPRO").count() == 0) {
            Family family = new Family();
            family.code = "DEVPRO";
            family.name = "Desenvolvimento de Produto";
            family.description = "Uma família de desenvolvedores de produto";
            familyRepository.persist(family);
            System.out.println(
                    "\n\n\n <<<<<<<<<<<<<<<<<<<< DEVPRO family created! >>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n");
        }

        // Creating Manoel
        if (userRepository.find("username", "manoel").count() == 0) {
            User user = new User();
            user.username = "manoel";
            user.password = BcryptUtil.bcryptHash("manoel");
            user.name = "Manoel Rodrigues";
            user.roles.add(roleRepository.find("name", "parent").firstResult());
            user.roles.add(roleRepository.find("name", "admin").firstResult());
            userRepository.persist(user);
            System.out.println(
                    "\n\n\n <<<<<<<<<<<<<<<<<<<< Manoel created! >>>>>>>>>>>>>>>>>>>>>>>>\n\n\n\n");
        }

    }

}
