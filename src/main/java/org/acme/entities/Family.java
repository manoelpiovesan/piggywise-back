package org.acme.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @Column(name = "description")
    public String description;

    @OneToMany(mappedBy = "family")
    public List<User> users;
}
