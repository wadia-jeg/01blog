package com.zone._blog.follows.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record FollowRequest(
        @NotNull(message = "To follow someone you must include his id")
        UUID followingId
        ) {

}
