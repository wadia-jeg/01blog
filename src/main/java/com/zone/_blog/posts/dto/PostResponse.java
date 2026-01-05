package com.zone._blog.posts.dto;

import java.time.Instant;
import java.util.UUID;

public class PostResponse {

    private UUID id;

    private UUID userId;

    private String title;

    private String content;

    private Instant createdAt;

    private boolean isDeleted = false;

    protected PostResponse() {
    }

    public PostResponse(UUID id, UUID userId, String title, String content, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    // public static PostResponse from(Post post) {
    //     UUID userId = post.getUser() != null ? post.getUser().getId() : null;
    //     return new PostResponse(post.getId(), userId, post.getTitle(), post.getContent(), post.getCreatedAt());
    // }
    // public static PostResponse from(PostRequest postRequest) {
    //     return new PostResponse(postRequest.getId(), postRequest.getUserId(), postRequest.getTitle(), postRequest.getContent(), postRequest.getCreatedAt());
    // }
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
        return String.format("Post Dto[\n  id = %s,\n  title = %s,\n   content = %s,\n   creation date = %s]", this.getId(), this.getTitle(), this.getContent(), this.getCreatedAt());
    }

}
