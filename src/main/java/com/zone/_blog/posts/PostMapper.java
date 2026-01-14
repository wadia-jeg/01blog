package com.zone._blog.posts;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;
import com.zone._blog.users.UserInfo;

@Component
public class PostMapper {

    public static PostResponse toPostResponse(Post post) {
        UUID userId = post.getUser() != null ? post.getUser().getId() : null;

        return new PostResponse(
                post.getId(),
                userId,
                post.getTitle(),
                post.getContent(),
                post.getMedia() != null ? post.getMedia().getId() : null,
                post.getCreatedAt(),
                post.getIsDeleted()
        );
    }

    public static PostRequest toPostRequest(Post post) {
        UUID userId = post.getUser() != null ? post.getUser().getId() : null;
        return new PostRequest(
                userId,
                post.getTitle(),
                post.getContent()
        );
    }

    public static Post toPost(PostRequest postRequest, UserInfo user) {
        // UserInfo user = this.userRepository.getReferenceById(postRequest.getUserId());

        return new Post(
                user,
                postRequest.getTitle(),
                postRequest.getContent(),
                null,
                null
        );
    }
}
