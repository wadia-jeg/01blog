package com.zone._blog.follows;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zone._blog.follows.dto.FollowResponse;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {

    boolean existsByFollowingIdAndFollowerId(UUID followingId, UUID followerId);

    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    List<FollowResponse> findAllByFollowerId(UUID followerId);

    List<FollowResponse> findAllByFollowingId(UUID followingId);

    Optional<Follow> findByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

}
