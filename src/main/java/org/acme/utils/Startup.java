package org.acme.utils;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.repositories.PiggyRepository;
import org.acme.repositories.TaskRepository;
import org.acme.repositories.UserRepository;

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

    @Transactional
    void onStart(@Observes StartupEvent ev) {


    }

}
