package org.acme.repositories;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.acme.entities.User;

import java.util.Date;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class UserRepository
        implements PanacheRepository<User> {

    @Transactional
    public User create(User user) {
        user.createdAt = new Date();
        user.updatedAt = new Date();
        user.password = BcryptUtil.bcryptHash(user.password);
        persist(user);
        return user;
    }

}
