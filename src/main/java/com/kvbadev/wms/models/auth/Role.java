package com.kvbadev.wms.models.auth;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

}
