package com.zone._blog.follows.dto;

import com.zone._blog.users.UserInfo;

import java.time.Instant;

public record FollowResponse(
        UserInfo follower,
        UserInfo following,
        Instant followedAt
        ) {

}
