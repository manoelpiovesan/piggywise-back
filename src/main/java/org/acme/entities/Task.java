package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.acme.enums.TaskStatus;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.List;
import java.util.Set;

/**
 * @author Manoel Rodrigues
 */
@Entity
@Table(name = "tasks")
@SQLDelete(sql = "UPDATE tasks SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at = '1970-01-01 00:00:00+00'")
public class Task extends AbstractFullEntity {

    public String title;

    public String description;

    public int points;

    @Enumerated(EnumType.STRING)
    public TaskStatus status;

    @ManyToMany
    @JoinTable(
            name = "task_members",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public List<User> members;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    public Piggy piggy;

}
