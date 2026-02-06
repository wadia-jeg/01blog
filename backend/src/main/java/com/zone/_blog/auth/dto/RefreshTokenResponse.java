package com.zone._blog.auth.dto;

import java.time.Instant;

import com.zone._blog.users.dto.UserResponse;

public record RefreshTokenResponse(
        UserResponse user,
        String token,
        Instant expiration
        ) {

}
