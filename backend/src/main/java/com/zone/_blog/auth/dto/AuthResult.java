package com.zone._blog.auth.dto;

public record AuthResult(
        RefreshTokenResponse refreshToken,
        String accesstoken
        ) {

}
