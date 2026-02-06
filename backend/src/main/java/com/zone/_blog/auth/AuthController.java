package com.zone._blog.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.auth.dto.AuthRequest;
import com.zone._blog.auth.dto.AuthResponse;
import com.zone._blog.auth.dto.AuthResult;
import com.zone._blog.auth.dto.RefreshTokenResponse;

@RestController
@RequestMapping("${app.api.v1}/auth")
public class AuthController {

    @Value("${app.api.v1}")
    private String baseUrl;

    @Value("${spring-security.refresh-token.expiration}")
    private long refreshExpiration;

    private final AuthService authService;
    private final CookieService cookieService;

    public AuthController(
            AuthService authService,
            CookieService cookieService
    ) {
        this.authService = authService;
        this.cookieService = cookieService;

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResult authResult = this.authService.login(authRequest);
        RefreshTokenResponse refreshToken = authResult.refreshToken();
        String accessToken = authResult.accesstoken();
        ResponseCookie cookie = this.cookieService.generateCookie(
                "refresh-token",
                refreshToken.token(),
                this.refreshExpiration,
                baseUrl + "/auth/"
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponse(
                    accessToken,
                    refreshToken.user().id(),
                    refreshToken.user().username(),
                    refreshToken.user().role()
                    ));

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue("refresh-token") String refreshToken) {
        this.authService.logout(refreshToken);
        this.cookieService.revokeCookie("refresh-token");
        return ResponseEntity.status(HttpStatus.OK).body("Logged out succefully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue("refresh-token") String refreshToken) {
        AuthResult authResult = this.authService.refreshToken(refreshToken);
        RefreshTokenResponse newRefreshToken = authResult.refreshToken();
        String newAccessToken = authResult.accesstoken();

        ResponseCookie cookie = this.cookieService.generateCookie(
                "refresh-token",
                newRefreshToken.token(),
                this.refreshExpiration,
                baseUrl + "/auth/"
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponse(
                    newAccessToken,
                    newRefreshToken.user().id(),
                    newRefreshToken.user().username(),
                    newRefreshToken.user().role()
                    ));
    }

}
