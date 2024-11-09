package org.acme.projections;

import io.quarkus.hibernate.orm.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.entities.User;

import java.util.List;

/**
 * @author Manoel Rodrigues
 */
@RegisterForReflection
public class FamilyProjection {

    @ProjectedFieldName("f.id")
    public Long id;

    @ProjectedFieldName("f.name")
    public String name;

    @ProjectedFieldName("f.code")
    public String code;

    @ProjectedFieldName("f.description")
    public String description;

    @ProjectedFieldName("COALESCE(COUNT(u.id), 0)")
    public Long usersQty;

    public FamilyProjection(
            Long id,
            String name,
            String code,
            String description,
            Long usersQty
    ) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.usersQty = usersQty;
    }
}
