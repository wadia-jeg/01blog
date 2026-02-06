package com.zone._blog.auth;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.zone._blog.auth.dto.RefreshTokenResponse;
import com.zone._blog.exceptions.UnauthorizedException;
import com.zone._blog.users.UserInfo;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {

    @Value("${spring-security.refresh-token.expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshTokenResponse generateRefreshToken(UserInfo user) {
        RefreshToken token = new RefreshToken(
                user,
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshExpiration)
        );

        this.refreshTokenRepository.save(token);

        return RefreshTokenMapper.toRefreshTokenResponse(token);
    }

    @Transactional
    public RefreshTokenResponse refreshToken(String refreshToken) {

        RefreshToken refreshTokenEntity = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (isExpired(refreshTokenEntity)) {
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new BadCredentialsException("Refresh token expired");
        }

        refreshTokenEntity.rotate(this.refreshExpiration);

        return RefreshTokenMapper.toRefreshTokenResponse(refreshTokenEntity);
    }

    public void deleteRefreshToken(String refreshToken, UserInfo user) {

        RefreshToken refreshTokenEntity = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (!user.getId().equals(refreshTokenEntity.getUser().getId())) {

            throw new UnauthorizedException("Unauthorized access");
        }

        this.refreshTokenRepository.deleteById(refreshTokenEntity.getId());
    }

    public boolean isExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiration().isBefore(Instant.now());
    }
}
