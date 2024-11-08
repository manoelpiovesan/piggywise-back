package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "name")
    public String name;

    @Roles
    @ManyToMany
    public List<Role> roles = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    public Family family;

}
