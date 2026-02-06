package com.zone._blog.auth;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieService {

    public ResponseCookie generateCookie(String name, String value, long expiration, String path) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path(path)
                .sameSite("Strict")
                .maxAge(expiration)
                .build();
    }

    public void revokeCookie(String name) {
        ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(true)
                .path("")
                .sameSite("Strict")
                .maxAge(0)
                .build();
    }

}
