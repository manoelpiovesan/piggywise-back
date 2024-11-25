package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entities.Piggy;
import org.acme.entities.Reward;
import org.acme.entities.User;
import org.acme.enums.RewardStatus;

/**
 * @author Manoel Rodrigues
 */
@ApplicationScoped
public class RewardRepository implements PanacheRepository<Reward> {

    @Inject
    UserRepository userRepository;

    @Inject
    PiggyRepository piggyRepository;

    public Response create(SecurityContext context, Reward reward,
                           String piggyCode) {
        User user = userRepository.findByUsername(
                context.getUserPrincipal().getName());

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Piggy piggy = piggyRepository.find("code", piggyCode).firstResult();

        if (piggy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!piggy.family.code.equals(user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (user.roles.stream().noneMatch(role -> role.name.equals("parent"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        reward.piggy = piggy;

        persist(reward);
        return Response.ok(reward).build();
    }

    public Response claim(SecurityContext context, Long rewardId) {
        User user = userRepository.findByUsername(
                context.getUserPrincipal().getName());

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Reward reward = findById(rewardId);

        if (reward == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!reward.piggy.family.code.equals(user.family.code)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (!reward.piggy.family.users.contains(user)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (reward.status == RewardStatus.claimed) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        Piggy piggy = reward.piggy;
        piggyRepository.withdraw(context, piggy.code, reward.points);

        reward.status = RewardStatus.claimed;
        reward.claimedBy = user;
        persist(reward);

        return Response.ok(reward).build();
    }

}
