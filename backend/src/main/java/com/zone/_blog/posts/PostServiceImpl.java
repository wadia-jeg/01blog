package com.zone._blog.posts;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.auth.Role;
import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.exceptions.UnauthorizedException;
import com.zone._blog.likes.LikeService;
import com.zone._blog.media.MediaMapper;
import com.zone._blog.media.MediaService;
import com.zone._blog.media.dto.MediaDto;
import com.zone._blog.posts.dto.PostRequest;
import com.zone._blog.posts.dto.PostResponse;
import com.zone._blog.users.UserInfo;
import com.zone._blog.users.UserService;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final MediaService mediaService;
    private final LikeService likeService;

    public PostServiceImpl(
            PostRepository postRepository,
            UserService userService,
            MediaService mediaService,
            LikeService likeService
    ) {

        this.postRepository = postRepository;
        this.userService = userService;
        this.mediaService = mediaService;
        this.likeService = likeService;

    }

    @Override
    public PostResponse getPost(UUID postId) {
        UserInfo currentUser = this.userService.getCurrentUser();

        Post postEntity = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        long likes = this.likeService.getLikes(postEntity.getId());
        boolean isPostLiked = this.likeService.isPostLiked(postEntity.getId(), currentUser.getId());

        return PostMapper.toPostResponse(postEntity, likes, isPostLiked);

    }

    @Override
    public List<PostResponse> getPosts() {

        UserInfo currentUser = this.userService.getCurrentUser();

        return this.postRepository.findAllPosts(currentUser.getId());
    }

    @Override
    public List<PostResponse> getPostsByUser(UUID userId) {
        UserInfo currentUser = this.userService.getCurrentUser();

        return this.postRepository.findPostsByUserId(userId, currentUser.getId());
    }

    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest, MultipartFile media) {
        UserInfo currentUser = this.userService.getCurrentUser();

        Post postEntity = PostMapper.toPost(postRequest, null);
        if (media != null) {
            MediaDto mediaDto = this.mediaService.saveMedia(media);
            postEntity.setMedia(MediaMapper.toMedia(mediaDto));
        }

        postEntity.setUser(currentUser);
        this.postRepository.save(postEntity);

        return PostMapper.toPostResponse(postEntity);
    }

    @Override
    @Transactional
    public PostResponse updatePost(UUID postId, PostRequest postRequest, MultipartFile media) {
        UserInfo currentUser = this.userService.getCurrentUser();

        Post postEntity = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (currentUser.getId() != postEntity.getUser().getId()) {
            throw new UnauthorizedException("Unauthorized access");
        }

        if (media != null) {
            MediaDto mediaDto = this.mediaService.updateMedia(media, postEntity.getMedia().getId());
            postEntity.setMedia(MediaMapper.toMedia(mediaDto));
        }

        postEntity.setTitle(postRequest.title());
        postEntity.setContent(postRequest.content());
        this.postRepository.save(postEntity);

        return this.postRepository.findPostById(currentUser.getId(), postId).orElseThrow(() -> new ResourceNotFoundException("Post not found")
        );

    }

    @Override
    @Transactional
    public void deletePost(UUID postId) {
        UserInfo currentUser = this.userService.getCurrentUser();

        Post postEntity = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found")
        );

        if (currentUser.getId() != postEntity.getUser().getId()
                || currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Unauthorized access");
        }

        if (postEntity.getMedia() != null) {
            this.mediaService.deleteMedia(postEntity.getMedia().getId());
        }

        this.postRepository.deleteById(postId);
    }

}
