package com.zone._blog.users;

import com.zone._blog.auth.Role;
import com.zone._blog.users.dto.UserRequest;
import com.zone._blog.users.dto.UserResponse;

public class UserMapper {

    public static UserInfo toUser(UserRequest userRequest) {
        return new UserInfo(
                userRequest.email(),
                userRequest.username(),
                userRequest.firstname(),
                userRequest.lastname(),
                Role.REGULAR
        );

    }

    public static UserResponse toUserResponse(UserInfo userEntity, boolean isFollowed, boolean isFollowing) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getRole(),
                userEntity.getJoinedAt(),
                userEntity.isAccountNonLocked(),
                isFollowed,
                isFollowing,
                userEntity.getProfilePicture() != null ? userEntity.getProfilePicture().getId() : null
        );

    }

    public static UserResponse toUserResponse(UserInfo userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getRole(),
                userEntity.getJoinedAt(),
                userEntity.isAccountNonLocked(),
                false,
                false,
                userEntity.getProfilePicture() != null ? userEntity.getProfilePicture().getId() : null
        );

    }
}
