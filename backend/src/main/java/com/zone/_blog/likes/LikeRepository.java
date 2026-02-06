package com.zone._blog.likes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

    public boolean existsByUserIdAndPostId(UUID userId, UUID postId);

    public Optional<Like> findByUserIdAndPostId(UUID userId, UUID postId);

    public long countByPostId(UUID postId);

    @Query(
            "SELECT l.post.id AS postId, "
            + "COUNT(l) AS likeCount "
            + "FROM Like l "
            + "WHERE l IN: postIds "
            + "GROUP BY l.post.id"
    )
    public List<Object[]> countLikesForPosts(@Param("postIds") List<UUID> postIds);

    @Query(
            "SELECT l.post.id FROM Like l "
            + "WHERE l.user.id = :userId "
            + "AND l.post.id IN :postIds"
    )
    public Set<UUID> findLikedPostsByUserId(@Param("userId") UUID userId, @Param("postIds") List<UUID> postIds);

}
