package com.zone._blog.likes;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.likes.dto.LikeResponse;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("${app.api.v1}")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/posts/{postId}/likes/count")
    public ResponseEntity<Long> getLikes(
            @NotBlank(message = "To get the count like must have a post") @PathVariable UUID postId) {

        long likeCount = this.likeService.getLikes(postId);

        return ResponseEntity.ok(likeCount);
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<LikeResponse> toggleLike(
            @NotBlank(message = "A like must have a post") @PathVariable UUID postId) {
        LikeResponse likeResponse = this.likeService.toggleLike(postId);

        return ResponseEntity.status(HttpStatus.CREATED).body(likeResponse);
    }

}
