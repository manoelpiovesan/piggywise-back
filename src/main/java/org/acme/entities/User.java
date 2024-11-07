package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import org.acme.enums.ProfileType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.Set;

/**
 * @author Manoel Rodrigues
 */
@Entity
@UserDefinition
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at = '1970-01-01 00:00:00+00'")
public class User extends AbstractFullEntity {

    @Username
    public String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Password
    public String password;

    @Roles
    public String role;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type", nullable = false)
    public ProfileType profileType;

    @JsonIgnore
    @ManyToMany(mappedBy = "members")
    public List<Task> tasks;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "parents")
    public List<User> children;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany
    @JoinTable(
            name = "user_parents",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    public List<User> parents;

    @JsonIgnore
    @ManyToMany(mappedBy = "members")
    public List<Piggy> piggies;

}
