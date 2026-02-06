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

}
