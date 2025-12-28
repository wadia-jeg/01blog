package com.zone._blog.comments;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Comment{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Integer id;

    private Integer commentI;
    private Integer postId;
    private String content;
    private LocalDateTime createdAt;

}