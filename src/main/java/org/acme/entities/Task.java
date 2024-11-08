package org.acme.entities;

import jakarta.persistence.*;
import org.acme.enums.TaskStatus;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * @author Manoel Rodrigues
 */
@Entity
@Table(name = "tasks")
@SQLDelete(sql = "UPDATE tasks SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at = '1970-01-01 00:00:00+00'")
public class Task extends AbstractFullEntity {

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "points")
    public int points;

    @Enumerated(EnumType.STRING)
    public TaskStatus status = TaskStatus.PENDING;

    @ManyToOne
    public Piggy piggy;

}
