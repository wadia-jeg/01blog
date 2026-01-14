package com.zone._blog.posts.dto;

import java.time.Instant;
import java.util.UUID;

public record PostResponse(
        UUID id,
        UUID userId,
        String title,
        String content,
        UUID media,
        Instant createdAt,
        boolean isDeleted
        ) {

    public UUID getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public UUID getMedia() {
        return this.media;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    @Override
    public String toString() {
        return String.format("Post Response[\n  id = %s,\n  title = %s,\n   content = %s,\n   creation date = %s\n    media = %s]", this.getId(), this.getTitle(), this.getContent(), this.getCreatedAt(), this.getMedia().toString());
    }

}
