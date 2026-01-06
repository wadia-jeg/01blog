package com.zone._blog.posts.dto;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostRequest {

    // add final if the feilds won't change
    @NotNull(message = "Post must have an id")
    private UUID id;

    @NotNull(message = "Post must have a user id")
    private UUID userId;

    @NotBlank(message = "Post must have a title")
    @Size(min = 5, max = 200, message = "Post title must have at least 5 charcters and no more than 200")
    private String title;

    @NotBlank(message = "Post must have content")
    @Size(min = 5, max = 20000, message = "Post title must have at least 5 charcters and no more than 200")
    private String content;

    @NotNull(message = "Post must have a creation date")
    private Instant createdAt;

    private boolean isDeleted;

    protected PostRequest() {
    }

    public PostRequest(UUID id, UUID userId, String title, String content, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
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
        return String.format("Post Dto[\n  id = %s,\n  title = %s,\n   content = %s,\n   creation date = %s]", this.getId(), this.getTitle(), this.getContent(), this.getCreatedAt().toString());
    }

}
