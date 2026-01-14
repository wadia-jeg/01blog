package com.zone._blog.posts;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("${app.api.v1}/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            // @RequestPart("file") MultipartFile media,
            // @Valid @RequestPart("content") PostRequest postRequest
            @Valid @RequestBody PostRequest postRequest
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.postService.createPost(postRequest, null));
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        return ResponseEntity.ok(this.postService.getPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable UUID id) {
        return ResponseEntity.ok(this.postService.getPost(id));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> updatePost(
            // @RequestPart("file") MultipartFile media,
            // @Valid @RequestPart("content") PostRequest postRequest,
            @Valid @RequestBody PostRequest postRequest,
            UUID id
    ) {

        return ResponseEntity.ok(this.postService.updatePost(id, postRequest, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@NotBlank(message = "To delete a post you must have an id") @PathVariable UUID id) {
        this.postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted Succefully");
    }

}
