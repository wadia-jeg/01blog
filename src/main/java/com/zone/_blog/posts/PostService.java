package com.zone._blog.posts;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;

public interface PostService {

    public List<PostResponse> getPosts();

    public PostResponse getPost(UUID postId);

    public PostResponse createPost(PostRequest postRequest, MultipartFile media);

    public PostResponse updatePost(UUID postId, PostRequest postRequest, MultipartFile media);

    public void deletePost(UUID postId);

}
