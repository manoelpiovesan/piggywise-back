package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Family;
import org.acme.entities.Piggy;
import org.acme.entities.User;

import java.util.Objects;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class PiggyRepository implements PanacheRepository<Piggy> {

    @Inject
    UserRepository userRepository;

    @Inject
    FamilyRepository familyRepository;

    public Response get(SecurityContext context) {
        User user = userRepository.find("username",
                                        context.getUserPrincipal().getName())
                                  .firstResult();

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(
                find("FROM Piggy p WHERE p.family.code = ?1",
                     user.family.code).list()).build();
    }

    public Response sync(
            SecurityContext context,
            String code,
            String name,
            String description
    ) {
        User user = userRepository.findByUsername(
                context.getUserPrincipal().getName());

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Piggy piggy = find("FROM Piggy p WHERE p.code = ?1", code)
                .firstResult();

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (piggy.family != null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        piggy.family = user.family;
        piggy.name = name;
        piggy.description = description;
        getEntityManager().merge(piggy);
        return Response.ok(piggy).build();
    }

    public Response getPiggiesByFamilyCode(String code) {
        Family family = familyRepository.find("code", code).firstResult();

        if (family == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(
                find("FROM Piggy p WHERE p.family.code = ?1",
                     code).list()).build();
    }

    public Response getPiggyById(SecurityContext context, Long id) {
        User user = userRepository.findByUsername(
                context.getUserPrincipal().getName());

        Piggy piggy = findById(id);

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!Objects.equals(piggy.family.code, user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.ok(piggy).build();
    }

    public Response deposit(String code,
                            Double value) {

        Piggy piggy = find("code", code).firstResult();

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        piggy.balance += value;
        piggy.persist();

        return Response.ok(piggy).build();
    }

    public Response withdraw(SecurityContext context, String code,
                             int value) {
        User user = userRepository.findByUsername(
                context.getUserPrincipal().getName());

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Piggy piggy = find("code", code).firstResult();

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!Objects.equals(piggy.family.code, user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (piggy.balance < value) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        piggy.balance -= value;
        getEntityManager().merge(piggy);

        return Response.ok(piggy).build();
    }

}
