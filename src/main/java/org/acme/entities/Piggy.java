package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@Entity
public class Piggy extends AbstractFullEntity{

    public String name;

    public String description;

    public int balance;

    @OneToMany(mappedBy = "piggy")
    public List<Task> tasks;

    @ManyToMany
    public List<User> members;

}
