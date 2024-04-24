package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.hateoas.server.core.Relation;

import java.beans.ConstructorProperties;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Relation(collectionRelation = "userList")
public class UserView {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    @JsonProperty("roles")
    private final Set<String> roleNames = new HashSet<>();

    @JsonCreator
    @ConstructorProperties({"id","firstName","lastName","email"})
    public UserView(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoleNames() {
        return roleNames;
    }
    public int getId() {
        return id;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserView userView)) return false;
        return id == userView.id && Objects.equals(firstName, userView.firstName) && Objects.equals(lastName, userView.lastName) && Objects.equals(email, userView.email) && Objects.equals(roleNames, userView.roleNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, roleNames);
    }

}
