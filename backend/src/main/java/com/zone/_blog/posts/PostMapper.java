package com.zone._blog.posts;

import org.springframework.stereotype.Component;

import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;
import com.zone._blog.users.UserInfo;

@Component
public class PostMapper {

    public static PostResponse toPostResponse(Post post, long likes, boolean isPostLiked) {

        return new PostResponse(
                post.getId(),
                post.getUser(),
                post.getTitle(),
                post.getContent(),
                post.getMedia() != null ? post.getMedia().getId() : null,
                post.getCreatedAt(),
                post.getIsDeleted(),
                likes,
                isPostLiked
        );
    }

    public static PostResponse toPostResponse(Post post) {

        return new PostResponse(
                post.getId(),
                post.getUser(),
                post.getTitle(),
                post.getContent(),
                post.getMedia() != null ? post.getMedia().getId() : null,
                post.getCreatedAt(),
                post.getIsDeleted(),
                0,
                false
        );
    }

    public static Post toPost(PostRequest postRequest, UserInfo user) {

        return new Post(
                user,
                postRequest.title(),
                postRequest.content()
        );
    }
}
