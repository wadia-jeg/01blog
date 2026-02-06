package com.zone._blog.likes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.likes.dto.LikeResponse;
import com.zone._blog.posts.Post;
import com.zone._blog.posts.PostRepository;
import com.zone._blog.users.UserInfo;
import com.zone._blog.users.UserService;

import jakarta.transaction.Transactional;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public LikeServiceImpl(
            LikeRepository likeRepository,
            PostRepository postRepository,
            UserService userService
    ) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    @Override
    public long getLikes(UUID postId) {
        return this.likeRepository.countByPostId(postId);
    }

    @Override
    public Map<UUID, Long> getLikesForPosts(List<UUID> postIds) {
        List<Object[]> likes = this.likeRepository.countLikesForPosts(postIds);

        return likes.stream()
                .collect(Collectors.toMap(
                        like -> (UUID) like[0],
                        like -> (Long) like[1]
                ));
    }

    @Override
    public Set<UUID> getLikedPostsByUser(UUID userId, List<UUID> postIds) {
        return this.likeRepository.findLikedPostsByUserId(userId, postIds);
    }

    @Transactional
    @Override
    public LikeResponse toggleLike(UUID postId
    ) {
        Post post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        UserInfo user = this.userService.getCurrentUser();

        Optional<Like> oldLike = this.likeRepository.findByUserIdAndPostId(user.getId(), postId);

        if (oldLike.isPresent()) {
            Like likeEntity = oldLike.get();
            this.likeRepository.delete(likeEntity);
            return LikeMapper.toLikeReponse(likeEntity);
        }

        Like newLike = new Like(user, post);

        this.likeRepository.save(newLike);

        return LikeMapper.toLikeReponse(newLike);
    }

    @Override
    public boolean isPostLiked(UUID postId, UUID userId
    ) {

        return this.likeRepository.existsByUserIdAndPostId(userId, postId);
    }

}
