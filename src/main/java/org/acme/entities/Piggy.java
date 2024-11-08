package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Entity
@Table(name = "piggies")
@SQLDelete(sql = "UPDATE piggies SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at = '1970-01-01 00:00:00+00'")
public class Piggy extends AbstractFullEntity{

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @JsonIgnore
    @OneToMany(mappedBy = "piggy")
    public List<Task> tasks;



}
