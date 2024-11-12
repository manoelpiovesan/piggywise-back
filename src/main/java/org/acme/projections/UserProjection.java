package org.acme.projections;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Manoel Rodrigues
 */
@RegisterForReflection
public class UserProjection {

    public Long id;
    public String username;
    public String name;

    public UserProjection(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

}
