package com.zone._blog.auth;

import java.time.Instant;
import java.util.UUID;

import com.zone._blog.users.UserInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private UserInfo user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiration;

    public RefreshToken() {
    }

    public RefreshToken(UserInfo user, String token, Instant expiration) {
        this.user = user;
        this.token = token;
        this.expiration = expiration;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserInfo getUser() {
        return this.user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiration() {
        return this.expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public void rotate(long expiration) {
        this.token = UUID.randomUUID().toString();
        this.expiration = Instant.now().plusMillis(expiration);
    }

}
