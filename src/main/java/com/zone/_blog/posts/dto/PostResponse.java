package com.zone._blog.posts.dto;

import java.time.Instant;
import java.util.UUID;

import com.zone._blog.media.dto.MediaDto;

public class PostResponse {

    private UUID id;

    private UUID userId;

    private String title;

    private String content;

    private UUID media;

    private Instant createdAt;

    private boolean isDeleted;

    protected PostResponse() {
    }

    public PostResponse(UUID id, UUID userId, String title, String content, Instant createdAt, UUID media) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.media = media;
    }

    public UUID getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getMedia() {
        return this.media;
    }

    public void setMedia(UUID media) {
        this.media = media;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUser(UUID userId) {
        this.userId = userId;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return String.format("Post Response[\n  id = %s,\n  title = %s,\n   content = %s,\n   creation date = %s\n    media = %s]", this.getId(), this.getTitle(), this.getContent(), this.getCreatedAt(), this.getMedia().toString());
    }

}
