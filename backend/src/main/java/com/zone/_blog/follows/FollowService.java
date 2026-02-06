package com.zone._blog.follows;

import java.util.List;
import java.util.UUID;

import com.zone._blog.follows.dto.FollowRequest;
import com.zone._blog.follows.dto.FollowResponse;

public interface FollowService {

    public boolean isUserFollowed(UUID followerId);

    public boolean isUserFollowing(UUID followingId);

    public List<FollowResponse> getFollowers(UUID userId);

    public List<FollowResponse> getFollowers();

    public List<FollowResponse> getFollowings(UUID userId);

    public List<FollowResponse> getFollowings();

    public FollowResponse toggleFollow(FollowRequest followRequest);

    public void unfollow(UUID followingId);

}
