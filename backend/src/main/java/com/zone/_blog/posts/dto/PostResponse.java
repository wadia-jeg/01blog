package com.zone._blog.posts.dto;

import com.zone._blog.users.UserInfo;

import java.time.Instant;
import java.util.UUID;

public record PostResponse(
        UUID id,
        UserInfo user,
        String title,
        String content,
        UUID media,
        Instant createdAt,
        boolean isDeleted,
        long likes,
        boolean isLikedByCurrentUser
        ) {

}
