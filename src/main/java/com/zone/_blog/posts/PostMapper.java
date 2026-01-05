package com.zone._blog.posts;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;
import com.zone._blog.users.User;
import com.zone._blog.users.UserRepository;

@Component
public class PostMapper {

    public static PostResponse toPostResponse(Post post) {
        UUID userId = post.getUser() != null ? post.getUser().getId() : null;

        return new PostResponse(post.getId(), userId, post.getTitle(), post.getContent(), post.getCreatedAt());
    }

    public static PostResponse toPostResponse(PostRequest postRequest) {
        return new PostResponse(postRequest.getId(), postRequest.getUserId(), postRequest.getTitle(), postRequest.getContent(), postRequest.getCreatedAt());
    }

    public static PostRequest toPostRequest(Post post) {
        UUID userId = post.getUser() != null ? post.getUser().getId() : null;
        return new PostRequest(post.getId(), userId, post.getTitle(), post.getContent(), post.getCreatedAt());
    }

    public static PostRequest toPostRequest(PostResponse postResponse) {

        return new PostRequest(postResponse.getId(), postResponse.getUserId(), postResponse.getTitle(), postResponse.getContent(), postResponse.getCreatedAt());
    }

    public static Post toPost(PostResponse postResponse, User user) {

        return new Post(postResponse.getId(), user, postResponse.getTitle(), postResponse.getContent(), postResponse.getCreatedAt());
    }

    public static Post toPost(PostRequest postRequest, User user) {
        // User user = this.userRepository.getReferenceById(postRequest.getUserId());

        return new Post(postRequest.getId(), user, postRequest.getTitle(), postRequest.getContent(), postRequest.getCreatedAt());
    }
}
