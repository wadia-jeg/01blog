package com.zone._blog.comments;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;

import com.zone._blog.posts.Post;
import com.zone._blog.users.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Comment() {}

    public Comment(User user, Post post, String content){
        this.user = user;
        this.post = post;
        this.content = content;
    }

    public UUID getId(){
        return this.id;
    }

    public String getContent(){
        return this.content;
    }
    
    public void setContent(String content){
        this.content = content;
    }

    public Instant getCreatedAt(){
        return this.createdAt;
    }

    public User getUser(){
        return this.user;
    }

    public void setuser(User user){
        this.user = user;
    }

    public Post getPost(){
        return this.post;
    }

    public void setPost(Post post){
        this.post = post;
    }
}