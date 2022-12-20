package com.example.customerprofile.domain;

import javax.validation.constraints.NotBlank;

/**
 * Request to update/change the customer profile. Only first and lastName can be changed.
 */
public class CustomerProfileChangeRequest {

    @NotBlank
    private final String firstName;

    @NotBlank
    private final String lastName;

    @NotBlank
    private final String email;

    public CustomerProfileChangeRequest(String firstName, String lastName, String email) {
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
}
