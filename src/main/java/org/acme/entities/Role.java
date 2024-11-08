package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.security.jpa.RolesValue;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Entity
@Table(name = "roles")
@SQLDelete(sql = "UPDATE roles SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at = '1970-01-01 00:00:00+00'")
public class Role extends AbstractFullEntity {

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    public List<User> users;

    @RolesValue
    public String role;

}
