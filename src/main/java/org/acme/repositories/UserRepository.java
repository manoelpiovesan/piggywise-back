package org.acme.repositories;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.User;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class UserRepository
        implements PanacheRepository<User> {

    @Inject
    RoleRepository roleRepository;

    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public Response createParent(User user) {
        if (user.username.contains(" ") || user.username.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Username cannot contain spaces or be empty!")
                           .build();
        }

        if (checkIfExist(user.username)) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        user.password = BcryptUtil.bcryptHash(user.password);
        user.roles.add(roleRepository.find("name", "parent").firstResult());
        persist(user);

        return Response.ok(user).build();

    }

    public Response createChild(User user) {
        if (user.username.contains(" ") || user.username.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Username cannot contain spaces or be empty!")
                           .build();
        }

        if (checkIfExist(user.username)) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        user.password = BcryptUtil.bcryptHash(user.password);
        user.roles.add(roleRepository.find("name", "child").firstResult());
        persist(user);

        return Response.ok(user).build();

    }

    public Boolean checkIfExist(String username) {
        User user = find("username", username).firstResult();
        return user != null;
    }

    public Response login(SecurityContext context) {
        User user = findByUsername(context.getUserPrincipal().getName());
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

}
