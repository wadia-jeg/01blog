package com.zone._blog.reports.reason;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity

public class ReportReason {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String type;

    public ReportReason() {

    }

    public ReportReason(String type) {
        this.type = type;
    }

    public UUID getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

}
