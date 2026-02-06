package com.zone._blog.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zone._blog.users.dto.UserResponse;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, UUID> {

    Optional<UserInfo> findByUsernameIgnoreCaseOrEmailIgnoreCase(
            String username,
            String email
    );

    boolean existsByUsernameIgnoreCaseOrEmailIgnoreCase(
            String username,
            String email
    );

    Optional<UserInfo> findByUsernameIgnoreCase(String username);

    @Query(
            """
            SELECT new com.zone._blog.users.dto.UserResponse(
                   u.id,
                   u.email,
                   u.username,
                   u.firstname,
                   u.lastname,
                   u.role,
                   u.joinedAt,
                   u.enabled,
                   CASE
                        WHEN EXISTS 
                                (SELECT 1 
                                FROM Follow f
                                        WHERE f.following.id = :currentUserId 
                                        AND  f.follower.id = u.id
                                ) THEN true ELSE false
                   END,
                   CASE
                        WHEN EXISTS 
                                (SELECT 1
                                FROM Follow f
                                        WHERE f.following.id =  u.id
                                        AND f.follower.id = :currentUserId
                                ) THEN true ELSE false
                   END,
                   u.profilePicture.id
                   )
           FROM UserInfo u
           WHERE u.id = :userId
        """
    )
    Optional<UserResponse> findUserById(@Param("userId") UUID userId, @Param("currentUserId") UUID currentUserId);

    @Query(
            """
            SELECT new com.zone._blog.users.dto.UserResponse(
                   u.id,
                   u.email,
                   u.username,
                   u.firstname,
                   u.lastname,
                   u.role,
                   u.joinedAt,
                   u.enabled,
                   CASE
                        WHEN EXISTS 
                                (SELECT 1
                                FROM Follow f
                                        WHERE f.following.id = :currentUserId  
                                        AND f.follower.id = u.id
                                ) THEN true ELSE false
                   END,
                   CASE
                        WHEN EXISTS 
                                (SELECT 1
                                FROM Follow f
                                        WHERE  f.following.id = u.id 
                                        AND f.follower.id = :currentUserId
                                ) THEN true ELSE false
                   END,
                   u.profilePicture.id
                   )
           FROM UserInfo u
           WHERE u.id <> :currentUserId
        """
    )
    List<UserResponse> findAllUsers(@Param("currentUserId") UUID currentUserId);

}
