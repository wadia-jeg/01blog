package com.zone._blog.moderation.dto;

import java.util.UUID;
import java.time.Instant;

public record PostByLike(
        UUID id,
        String title,
        String content,
        String username,
        Instant creaedAt,
        UUID mediaId,
        Long count
        ) {

}
