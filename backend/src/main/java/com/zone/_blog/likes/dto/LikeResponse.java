package com.zone._blog.likes.dto;

import java.time.Instant;
import java.util.UUID;

public record LikeResponse(
        UUID id,
        UUID post,
        UUID user,
        Instant createdAt
        ) {

}
