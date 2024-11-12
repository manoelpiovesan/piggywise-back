package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.acme.utils.RandomIDGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Entity
@Table(name = "families")
@SQLDelete(sql = "UPDATE families SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at = '1970-01-01 00:00:00+00'")
public class Family extends AbstractFullEntity{

    @Column(name = "name")
    public String name;

    @Column(name = "code")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String code;

    @Nullable
    @Column(name = "description")
    public String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "family", fetch = FetchType.EAGER)
    public List<User> users;

    @Nullable
    @JsonIgnore
    @OneToMany(mappedBy = "family", fetch = FetchType.EAGER)
    public List<Piggy> piggies;
}
