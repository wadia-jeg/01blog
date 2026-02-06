package com.zone._blog.auth.dto;

public record RefreshTokenRequest(String token) {

}


/*
@PostMapping("/refresh")
public ResponseEntity<TokenResponse> refresh(
        @RequestBody RefreshTokenRequest request
) {
    RefreshToken token = refreshTokenRepository
        .findByToken(request.getRefreshToken())
        .orElseThrow(() ->
            new BadCredentialsException("Invalid refresh token")
        );

    if (refreshTokenService.isExpired(token)) {
        refreshTokenRepository.delete(token);
        throw new BadCredentialsException("Refresh token expired");
    }

    // rotate
    refreshTokenRepository.delete(token);

    RefreshToken newToken =
        refreshTokenService.create(token.getUser());
    refreshTokenRepository.save(newToken);

    String newAccessToken =
        jwtService.generateToken(token.getUser());

    return ResponseEntity.ok(
        new TokenResponse(
            newAccessToken,
            newToken.getToken()
        )
    );
}

 */
