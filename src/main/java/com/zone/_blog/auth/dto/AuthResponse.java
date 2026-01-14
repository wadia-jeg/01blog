package com.zone._blog.auth.dto;

public record AuthResponse(
        String login,
        String token
        ) {

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

}
