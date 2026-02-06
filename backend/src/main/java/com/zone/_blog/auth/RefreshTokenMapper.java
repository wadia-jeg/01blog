package com.zone._blog.auth;

import com.zone._blog.auth.dto.RefreshTokenResponse;
import com.zone._blog.users.UserInfo;
import com.zone._blog.users.UserMapper;

public class RefreshTokenMapper {

    public static RefreshTokenResponse toRefreshTokenResponse(RefreshToken refreshTokenEntity) {
        return new RefreshTokenResponse(
                UserMapper.toUserResponse(refreshTokenEntity.getUser()),
                refreshTokenEntity.getToken(),
                refreshTokenEntity.getExpiration()
        );
    }

    public static RefreshToken toRefreshToken(RefreshTokenResponse refreshTokenResponse, UserInfo user) {
        return new RefreshToken(
                user,
                refreshTokenResponse.token(),
                refreshTokenResponse.expiration()
        );
    }

}
