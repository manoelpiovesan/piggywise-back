package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
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
public class Piggy extends AbstractFullEntity {

    @Column(name = "code")
    public String code;

    @Column(name = "balance")
    public int balance;

    @Nullable
    @Column(name = "name")
    public String name;

    @Nullable
    @Column(name = "description")
    public String description;

    @Nullable
    @OneToMany(mappedBy = "piggy", fetch = FetchType.EAGER)
    public List<Task> tasks;

    @Nullable
    @OneToMany(mappedBy = "piggy", fetch = FetchType.EAGER)
    public List<Reward> rewards;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "family_id")
    public Family family;

}
