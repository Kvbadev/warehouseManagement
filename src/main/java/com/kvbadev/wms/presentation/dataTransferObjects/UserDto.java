package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.kvbadev.wms.models.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Objects;

public class UserDto {
    @NotBlank(message = "First name is mandatory")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    private final String firstName;
    @NotBlank(message = "Last name is mandatory")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    private final String lastName;
    @Email
    @NotBlank
    private final String email;
    @Password
    private final String password;
    private final List<Integer> rolesId;

    @JsonCreator
    @ConstructorProperties({"firstName","lastName","email","password","rolesId"})
    public UserDto(String firstName, String lastName, String email, String password, List<Integer> rolesId){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.rolesId = rolesId;
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

    public String getPassword() {
        return password;
    }

    public List<Integer> getRolesId() {
        return rolesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto userDto)) return false;
        return Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(email, userDto.email) && Objects.equals(password, userDto.password) && Objects.equals(rolesId, userDto.rolesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, password, rolesId);
    }
}
