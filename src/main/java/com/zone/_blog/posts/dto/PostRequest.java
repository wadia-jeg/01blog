package com.zone._blog.posts.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        UUID userId,
        @NotBlank(message = "Post must have a title")
        @Size(min = 5, max = 200, message = "Post title must have at least 5 charcters and no more than 200")
        String title,
        @NotBlank(message = "Post must have content")
        @Size(min = 5, max = 20000, message = "Post title must have at least 5 charcters and no more than 200")
        String content
        ) {

    // add final if the feilds won't change
    // @NotNull(message = "Post must have a user id")
    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public UUID getUserId() {
        return this.userId;
    }

    @Override
    public String toString() {
        return String.format("Post Dto[\n  title = %s,\n   content = %s,\n]", this.getTitle(), this.getContent());
    }

}
