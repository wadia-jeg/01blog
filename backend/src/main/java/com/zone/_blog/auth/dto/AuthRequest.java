package com.zone._blog.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "Username or Email is missing to login")
        String login,
        @NotBlank(message = "Password is missing to login")
        String password
        ) {

}
