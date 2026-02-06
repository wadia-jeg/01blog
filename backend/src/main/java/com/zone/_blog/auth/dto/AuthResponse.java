package com.zone._blog.auth.dto;

import com.zone._blog.auth.Role;

import java.util.UUID;

public record AuthResponse(
        String accesstoken,
        UUID userId,
        String username,
        Role role
        ) {

}
