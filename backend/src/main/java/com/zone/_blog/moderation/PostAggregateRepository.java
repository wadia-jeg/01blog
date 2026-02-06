package com.zone._blog.moderation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zone._blog.moderation.dto.PostByPeriod;
import com.zone._blog.moderation.dto.PostByUser;
import com.zone._blog.posts.Post;

@Repository
public interface PostAggregateRepository extends JpaRepository<Post, UUID> {

    @Query(
            """
        SELECT COUNT(p) AS count,
               FUNCTION('year', p.createdAt) AS year,
               FUNCTION('week', p.createdAt) AS period
        FROM Post p
        GROUP BY 
                 FUNCTION('year', p.createdAt),
                 FUNCTION('week', p.createdAt)
        ORDER BY 
                 FUNCTION('year', p.createdAt) DESC,
                 FUNCTION('week', p.createdAt) DESC
        """
    )

    public List<PostByPeriod> countPostsByWeek();

    @Query(
            """
        SELECT COUNT(p) AS count,
               FUNCTION('year', p.createdAt) AS year,
               FUNCTION('month', p.createdAt) AS period
        FROM Post p
        GROUP BY 
                 FUNCTION('year', p.createdAt),
                 FUNCTION('month', p.createdAt)
        ORDER BY 
                 FUNCTION('year', p.createdAt) DESC,
                 FUNCTION('month', p.createdAt) DESC
        """
    )

    public List<PostByPeriod> countPostsByMonth();

    @Query(
            """
        SELECT COUNT(p) AS count,
               FUNCTION('year', p.createdAt) AS year,
               NULL AS period
        FROM Post p
        GROUP BY 
                 FUNCTION('year', p.createdAt)
        ORDER BY 
                 FUNCTION('year', p.createdAt) DESC
        """
    )

    public List<PostByPeriod> countPostsByYear();

    @Query(
            """
    SELECT p.user.username AS username,
           COUNT(p) AS count
    FROM Post p
    GROUP BY p.user.username
    ORDER BY COUNT(p)
    """
    )
    public List<PostByUser> countPostsByUser();

    @Query(
            """
    SELECT p.id AS id,
           p.title AS title,
           p.content AS content,
           p.user.username as usernmae,
           p.createdAt AS createdAT,
           p.media.id AS madiaId,
           COUNT(l)
    FROM Post p
    LEFT JOIN Like l ON l.post.id = p.id
    GROUP BY p.id,
           p.title,
           p.content,
           p.user.username,
           p.createdAt,
           p.media.id
    ORDER BY COUNT(l)
    """
    )
    public List<PostByUser> countPostsByLike();
}
