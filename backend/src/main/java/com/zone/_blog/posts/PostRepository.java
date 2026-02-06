package com.zone._blog.posts;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zone._blog.posts.dto.PostResponse;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    public List<Post> findByUserId(UUID userId);
    

    @Query(
            """
          SELECT new com.zone._blog.posts.dto.PostResponse(
            p.id,
            p.user,
            p.title,
            p.content,
            p.media.id,
            p.createdAt,
            p.isDeleted,
            COUNT(DISTINCT l.id),
            MAX(CASE WHEN l.user.id = :currentUser THEN 1 ELSE 0 END) = 1
        )
        FROM Post p
        LEFT JOIN Like l ON l.post.id = p.id
        GROUP BY p.id, p.user.id, p.title, p.content, p.media.id, p.createdAt, p.isDeleted            
        """
    )
    public List<PostResponse> findAllPosts(@Param("currentUser") UUID currentUser);
    //  UUID id,
    //     UserInfo user,
    //     String title,
    //     String content,
    //     UUID media,
    //     Instant createdAt,
    //     boolean isDeleted,
    //     long likes,
    //     boolean isLikedByCurrentUser

    @Query(
            """
          SELECT new com.zone._blog.posts.dto.PostResponse(
            p.id,
            p.user,
            p.title,
            p.content,
            p.media.id,
            p.createdAt,
            p.isDeleted,
            COUNT(DISTINCT l.id),
            MAX(CASE WHEN l.user.id = :currentUser THEN 1 ELSE 0 END) = 1
        )
        FROM Post p
        LEFT JOIN Like l ON l.post.id = p.id
        WHERE p.id = :postId            
        GROUP BY p.id, p.user.id, p.title, p.content, p.media.id, p.createdAt, p.isDeleted
        """
    )
    public Optional<PostResponse> findPostById(@Param("currentUser") UUID currentUser, @Param("postId") UUID postId);

    @Query(
            """
        SELECT new com.zone._blog.posts.dto.PostResponse(
            p.id,
            p.user,
            p.title,
            p.content,
            p.media.id,
            p.createdAt,
            p.isDeleted,
            COUNT(DISTINCT l.id),
            MAX(CASE WHEN l.user.id = :currentUser THEN 1 ELSE 0 END) = 1
        )
        FROM Post p
        LEFT JOIN Like l ON l.post.id = p.id
        WHERE :userId IS NULL OR p.user.id = :userId
        GROUP BY p.id, p.user.id, p.title, p.content, p.media.id, p.createdAt, p.isDeleted        
        """
    )
    public List<PostResponse> findPostsByUserId(@Param("userId") UUID userId, @Param("currentUser") UUID currentUser);

}
