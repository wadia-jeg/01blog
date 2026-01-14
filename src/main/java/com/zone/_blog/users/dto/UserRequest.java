package com.zone._blog.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "You must choose a suitable a username")
        @Size(min = 3, max = 10)
        @Pattern(regexp = "^[a-zA-Z0-9.*'_\\-]+$")
        String username,
        @NotBlank(message = "You must enter an active email")
        @Email
        String email,
        @NotBlank(message = "You must enter an active email")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).*$",
                message = "Your password is invalid"
        )
        @Size(min = 8, max = 20)
        String password,
        @Size(min = 3, max = 20)
        String firstname,
        @Size(min = 3, max = 20)
        String lastname
        ) {

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
