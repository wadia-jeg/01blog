package com.zone._blog.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

interface OnCreate {

}

interface OnUpdate {

}

public record UserRequest(
        @NotBlank(
                groups = OnCreate.class,
                message = "You must choose a suitable a username"
        )
        @Size(
                groups = OnCreate.class,
                min = 3,
                max = 10
        )
        @Pattern(
                groups = OnCreate.class,
                regexp = "^[a-zA-Z0-9.*'_\\-]+$"
        )
        String username,
        @NotBlank(
                groups = OnCreate.class,
                message = "You must enter an active email"
        )
        @Email
        String email,
        @NotBlank(
                groups = OnCreate.class,
                message = "You must enter an active email"
        )
        @Pattern(
                groups = OnCreate.class,
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*]).*$",
                message = "Your password is invalid"
        )
        @Size(
                groups = OnCreate.class,
                min = 8,
                max = 20
        )
        String password,
        @Size(
                groups = {OnCreate.class, OnUpdate.class},
                min = 3,
                max = 20
        )
        String firstname,
        @Size(
                groups = {OnCreate.class, OnUpdate.class},
                min = 3,
                max = 20
        )
        String lastname
        ) {

}
