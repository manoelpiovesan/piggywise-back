package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Family;
import org.acme.entities.User;
import org.acme.projections.FamilyProjection;
import org.acme.utils.RandomIDGenerator;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class FamilyRepository implements PanacheRepository<Family> {

    @Inject
    UserRepository userRepository;

    public Response create(SecurityContext context, Family family) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.roles.stream().noneMatch(role -> role.name.equals("parent"))) {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity("User does not have permission to create a family!")
                           .build();
        }

        if (user.family != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("User already has a family!")
                           .build();
        }

        String randomCode = RandomIDGenerator.generate();
        while (find("code", randomCode).count() > 0) {
            randomCode = RandomIDGenerator.generate();
        }

        family.code = randomCode;
        persist(family);

        user.family = family;
        userRepository.persist(user);

        return Response.ok(user.family).build();
    }

    public Response join(SecurityContext context, String code) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.family != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("User already has a family!")
                           .build();
        }

        Family family = find("code", code).firstResult();

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Family not found!")
                           .build();
        }

        user.family = family;
        userRepository.persist(user);

        return Response.ok(family).build();
    }

    public Response getMembers(SecurityContext context) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.family == null) {
            return Response.ok().build();
        }

        Family family =
                find("code", user.family.code).firstResult();

        return Response.ok(family.users).build();
    }

    public Response getFamily(SecurityContext context) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.family == null) {
            return Response.ok().build();
        }

        FamilyProjection family = find("""
                                       FROM Family f
                                        LEFT JOIN f.users u
                                        WHERE f.code = ?1
                                        GROUP BY f.id, f.name, f.code, f.description
                                       """, user.family.code)
                .project(
                        FamilyProjection.class)
                .firstResult();

        return Response.ok(family).build();
    }

    public Response leave(SecurityContext context) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.family == null) {
            return Response.ok().build();
        }

        user.family = null;
        userRepository.getEntityManager().merge(user);

        return Response.ok().build();
    }

}
