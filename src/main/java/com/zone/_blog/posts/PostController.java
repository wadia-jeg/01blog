package com.zone._blog.posts;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        return ResponseEntity.ok(this.postService.getPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String id) {
        return ResponseEntity.ok(this.postService.getPost(UUID.fromString(id)));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.postService.createPost(postRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@Valid @RequestBody PostRequest postRequest, @NotBlank(message = "To update a post you must have an id") @PathVariable String id) {
        return ResponseEntity.ok(this.postService.updatePost(UUID.fromString(id), postRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@NotBlank(message = "To delete a post you must have an id") @PathVariable String id) {
        this.postService.deletePost(UUID.fromString(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
