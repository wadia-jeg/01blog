package com.zone._blog.posts;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;
import com.zone._blog.users.User;
import com.zone._blog.users.UserRepository;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;

    }

    @Override
    public PostResponse getPost(UUID postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post " + postId + "not found"));

        return new PostResponse(post.getId(), UUID.randomUUID(), post.getTitle(), post.getContent(), post.getCreatedAt());

    }

    @Override
    public List<PostResponse> getPosts() {
        return postRepository.findAll().stream().map(post -> PostMapper.toPostResponse(post)).toList();
    }

    @Override
    public PostResponse createPost(PostRequest postRequest) {

        // User user = this.userRepository.getReferenceById(postRequest.getUserId());
        // if (user == null) {
        //     throw new ResourceNotFoundException("User " + postRequest.getUserId() + "not found");
        // }
        Post post = PostMapper.toPost(postRequest, null);
        this.postRepository.save(post);
        return PostMapper.toPostResponse(post);
    }

    @Override
    public PostResponse updatePost(UUID postId, PostRequest postRequest) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post " + postId + "not found")
        );
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        this.postRepository.save(post);

        return PostMapper.toPostResponse(post);
    }

    @Override
    public void deletePost(UUID postId) {
        if (!this.postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post " + postId + "not found");
        }
        this.postRepository.deleteById(postId);
    }

}
