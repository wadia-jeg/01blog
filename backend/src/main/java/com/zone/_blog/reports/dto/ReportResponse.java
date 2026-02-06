package com.zone._blog.reports.dto;

import java.time.Instant;
import java.util.UUID;

import com.zone._blog.reports.ReportStatus;
import com.zone._blog.reports.ReportType;
import com.zone._blog.reports.reason.ReportReason;

public record ReportResponse(
        UUID id,
        UUID reporter,
        UUID targetId,
        String targetName,
        ReportType reportType,
        String details,
        ReportReason reason,
        Instant reportedAt,
        Instant resolvedAt,
        ReportStatus status
        ) {

}
