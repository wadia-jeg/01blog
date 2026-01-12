package com.zone._blog.users;

import com.zone._blog.users.dto.UserRequest;
import com.zone._blog.users.dto.UserResponse;

public class UserMapper {

    public static UserInfo toUser(UserRequest userRequest) {
        return new UserInfo(
                userRequest.getEmail(),
                userRequest.getPassword(),
                userRequest.getUsername(),
                userRequest.getFirstname(),
                userRequest.getLastname(),
                userRequest.getRole()
        );

    }

    public static UserResponse toUserResponse(UserInfo userEntity) {
        return new UserResponse(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getUsername(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getRole(),
                userEntity.getJoinedAt()
        );

    }
}
