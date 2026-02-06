package com.zone._blog.follows;

import com.zone._blog.follows.dto.FollowResponse;

public class FollowMapper {

    public static FollowResponse toFollowResponse(Follow followEntity) {
        return new FollowResponse(
                followEntity.getFollower(),
                followEntity.getFollowing(),
                followEntity.getFollowedAt()
        );

    }

    // public static Follow toFollow(UserInfo follower, UserInfo following) {
    //     return new Follow(
    //             follower,
    //             following
    //     );
    // }
}
