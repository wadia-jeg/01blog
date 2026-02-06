package com.zone._blog.likes;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.zone._blog.likes.dto.LikeResponse;

public interface LikeService {

    public long getLikes(UUID postId);

    public LikeResponse toggleLike(UUID postId);

    public boolean isPostLiked(UUID postId, UUID userId);

    public Map<UUID, Long> getLikesForPosts(List<UUID> postIds);

    public Set<UUID> getLikedPostsByUser(UUID userId, List<UUID> postIds);

}
