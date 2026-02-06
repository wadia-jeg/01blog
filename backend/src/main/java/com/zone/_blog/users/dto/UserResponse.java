package com.zone._blog.users.dto;

import java.time.Instant;
import java.util.UUID;

import com.zone._blog.auth.Role;

public record UserResponse(
        UUID id,
        String email,
        String username,
        String firstname,
        String lastname,
        Role role,
        Instant joinedAt,
        boolean isBanned,
        boolean isFollowed,
        boolean isFollowing,
        UUID profilePicture
        ) {

}
