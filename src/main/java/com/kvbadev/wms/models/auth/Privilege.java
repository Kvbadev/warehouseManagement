package com.kvbadev.wms.models.auth;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "privileges")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

    public Privilege() {
    }
    public Privilege(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Collection<Role> getRoles() {
        return Collections.unmodifiableCollection(roles);
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
