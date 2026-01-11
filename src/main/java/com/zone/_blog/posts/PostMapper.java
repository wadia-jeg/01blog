package com.zone._blog.posts;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.zone._blog.media.Media;
import com.zone._blog.media.MediaMapper;
import com.zone._blog.media.dto.MediaContent;
import com.zone._blog.media.dto.MediaDto;
import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;
import com.zone._blog.users.User;

@Component
public class PostMapper {

    public static PostResponse toPostResponse(Post post) {
        UUID userId = post.getUser() != null ? post.getUser().getId() : null;

        return new PostResponse(
                post.getId(),
                userId,
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getMedia() != null ? post.getMedia().getId() : null
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

    public static Post toPost(PostResponse postResponse, User user, Media media) {

        return new Post(
                user,
                postResponse.getTitle(),
                postResponse.getContent(),
                postResponse.getCreatedAt(),
                media
        );

    }

    public static Post toPost(PostRequest postRequest, User user, Media media) {
        // User user = this.userRepository.getReferenceById(postRequest.getUserId());

        return new Post(
                user,
                postRequest.getTitle(),
                postRequest.getContent(),
                null,
                null
        );
    }
}
