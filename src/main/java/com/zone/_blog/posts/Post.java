package com.zone._blog.posts;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.zone._blog.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private boolean isDeleted = false;

    protected Post() {
    }

    public Post(UUID id, User user, String title, String content, Instant createdAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    // public static Post from(PostResponse postResponse, UserRepository userRepository) {
    //     User user = userRepository.getReferenceById(postResponse.getUserId());
    //     return new Post(postResponse.getId(), user, postResponse.getTitle(), postResponse.getContent(), postResponse.getCreatedAt());
    // }
    // public static Post from(PostRequest postRequest, UserRepository userRepository) {
    //     User user = userRepository.getReferenceById(postRequest.getUserId());
    //     return new Post(postRequest.getId(), user, postRequest.getTitle(), postRequest.getContent(), postRequest.getCreatedAt());
    // }
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return String.format("Post Entity[\n  id = %s,\n  title = %s,\n   content = %s,\n   creation date = %s]", this.getId(), this.getTitle(), this.getContent(), this.getCreatedAt());
    }

}
