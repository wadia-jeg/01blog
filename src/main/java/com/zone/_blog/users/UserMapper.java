package com.zone._blog.users;

import com.zone._blog.auth.Role;
import com.zone._blog.users.dto.UserRequest;
import com.zone._blog.users.dto.UserResponse;

public class UserMapper {

    public static UserInfo toUser(UserRequest userRequest) {
        return new UserInfo(
                userRequest.getEmail(),
                userRequest.getUsername(),
                userRequest.getFirstname(),
                userRequest.getLastname(),
                Role.REGULAR
        );

    }

    public static UserInfo toUser(UserResponse userResponse) {
        return new UserInfo(
                userResponse.getEmail(),
                userResponse.getUsername(),
                userResponse.getFirstname(),
                userResponse.getLastname(),
                Role.REGULAR
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
                userEntity.getProfilePicture() != null ? userEntity.getProfilePicture().getId() : null
        );

    }
}
