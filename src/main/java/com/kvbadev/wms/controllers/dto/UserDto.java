package com.kvbadev.wms.controllers.dto;

public class UserDto {
    public UserDto(String firstName, String lastName, String email, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.enabled = enabled;
    }

    public UserDto() {
    }
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
