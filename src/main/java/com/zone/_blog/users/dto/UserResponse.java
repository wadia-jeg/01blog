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
        UUID profilePicture
        ) {

    public UUID getId() {
        return id;
    }

    public Instant getJoinedAt() {
        return joinedAt;
    }

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

    public Role getRole() {
        return role;
    }

    public UUID getProfilePicture() {
        return profilePicture;
    }

}
