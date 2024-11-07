package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.Date;

/**
 * @author Manoel Rodrigues
 */
@MappedSuperclass
public class AbstractFullEntity extends AbstractEntity {

    @JsonIgnore
    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "timestamp with time zone")
    public Date createdAt = new Date();

    @Column(name = "updated_at", nullable = false,
            columnDefinition = "timestamp with time zone")
    public Date updatedAt = new Date();

    @JsonIgnore
    @Column(name = "deleted_at", nullable = false,
            columnDefinition = "timestamp with time zone")
    public Date deletedAt = new Date(0);

}
