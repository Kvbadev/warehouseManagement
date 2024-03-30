package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Objects;

public class UserPutRequest extends UserDto{
    private final Integer id;

    public Integer getId() {
        return id;
    }

    @JsonCreator
    @ConstructorProperties({"id","firstName","lastName","email","password","rolesId"})
    public UserPutRequest(Integer id, String firstName, String lastName, String email, String password, List<Integer> rolesId) {
        super(firstName, lastName, email, password, rolesId);
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPutRequest that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
