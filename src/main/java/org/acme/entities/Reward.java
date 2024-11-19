package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.acme.enums.RewardStatus;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * @author Manoel Rodrigues
 */
@Entity
@Table(name = "rewards")
@SQLDelete(sql = "UPDATE rewards SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at = '1970-01-01 00:00:00+00'")
public class Reward extends AbstractFullEntity {

    @Column(name = "name")
    public String name;

    @Nullable
    @Column(name = "description")
    public String description;

    @Column(name = "points")
    public int points;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public RewardStatus status = RewardStatus.available;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "piggy_id")
    public Piggy piggy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User claimedBy;

}
