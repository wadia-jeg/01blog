package com.zone._blog.posts;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.media.MediaMapper;
import com.zone._blog.media.MediaService;
import com.zone._blog.media.dto.MediaContent;
import com.zone._blog.media.dto.MediaDto;
import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;
import com.zone._blog.users.UserRepository;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    // private final UserRepository userRepository;
    private final MediaService mediaService;

    public PostServiceImpl(
            PostRepository postRepository,
            UserRepository userRepository,
            MediaService mediaService
    ) {

        this.postRepository = postRepository;
        // this.userRepository = userRepository;
        this.mediaService = mediaService;
    }

    @Override
    public PostResponse getPost(UUID postId) {
        Post postEntity = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post " + postId + "not found"));

        return PostMapper.toPostResponse(postEntity);

    }

    @Override
    public List<PostResponse> getPosts() {
        return postRepository
                .findAll()
                .stream()
                .map(PostMapper::toPostResponse)
                .toList();
    }

    @Override
    @Transactional

    public PostResponse createPost(PostRequest postRequest, MultipartFile media) {

        // User user = this.userRepository.getReferenceById(postRequest.getUserId());
        // if (user == null) {
        //     throw new ResourceNotFoundException("User " + postRequest.getUserId() + "not found");
        // }
        MediaDto mediaDto = this.mediaService.saveMedia(media);
        Post postEntity = PostMapper.toPost(postRequest, null, MediaMapper.toMedia(mediaDto));
        this.postRepository.save(postEntity);

        return PostMapper.toPostResponse(postEntity);
    }

    @Override
    @Transactional

    public PostResponse updatePost(UUID postId, PostRequest postRequest, MultipartFile media) {
        Post postEntity = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post " + postId + "not found")
        );

        MediaDto mediaDto = this.mediaService.saveMedia(media);

        postEntity.setTitle(postRequest.getTitle());
        postEntity.setContent(postRequest.getContent());
        postEntity.setMedia(MediaMapper.toMedia(mediaDto));
        this.postRepository.save(postEntity);

        return PostMapper.toPostResponse(postEntity);
    }

    @Override
    @Transactional
    public void deletePost(UUID postId) {
        Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post " + postId + "not found")
        );

        if (post.getMedia() != null) {
            this.mediaService.deleteMedia(post.getMedia().getId());
        }

        this.postRepository.deleteById(postId);
    }

}
