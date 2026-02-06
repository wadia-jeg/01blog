package com.zone._blog.reports;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.zone._blog.reports.reason.ReportReason;
import com.zone._blog.users.UserInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"reporter_id", "target_id", "report_type"}))
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private UserInfo reporter;

    @Column(nullable = false)
    private UUID targetId;

    @Column(nullable = false)
    private String targetName;

    @Column(nullable = false, length = 500)
    private String details;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_id", nullable = false)
    private ReportReason reason;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant reportedAt;

    private Instant resolvedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    public Report() {
    }

    public Report(UserInfo reporter, UUID targetId, ReportType reportType, String details, ReportReason reason) {
        this.reporter = reporter;
        this.targetId = targetId;
        this.reportType = reportType;
        this.details = details;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserInfo getReporter() {
        return this.reporter;
    }

    public void setReporter(UserInfo reporter) {
        this.reporter = reporter;
    }

    public UUID getTargetId() {
        return this.targetId;
    }

    public void setTargetId(UUID targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetNAme(String targetName) {
        this.targetName = targetName;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ReportReason getReportReason() {
        return this.reason;
    }

    public void setReportReason(ReportReason reason) {
        this.reason = reason;
    }

    public Instant getReportedAt() {
        return this.reportedAt;
    }

    public void setReportedAt(Instant reportedAt) {
        this.reportedAt = reportedAt;
    }

    public Instant getResolvedAt() {
        return this.resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public ReportType getReportType() {
        return this.reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public ReportStatus getStatus() {
        return this.status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }
}
