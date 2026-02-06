package com.zone._blog.reports;

import com.zone._blog.reports.dto.ReportRequest;
import com.zone._blog.reports.dto.ReportResponse;
import com.zone._blog.reports.reason.ReportReason;
import com.zone._blog.users.UserInfo;

public class ReportMapper {

    public static ReportResponse toReportResponse(Report reportEntity) {
        return new ReportResponse(
                reportEntity.getId(),
                reportEntity.getReporter().getId(),
                reportEntity.getTargetId(),
                reportEntity.getTargetName(),
                reportEntity.getReportType(),
                reportEntity.getDetails(),
                reportEntity.getReportReason(),
                reportEntity.getReportedAt(),
                reportEntity.getResolvedAt(),
                reportEntity.getStatus()
        );
    }

    public static Report toReport(
            ReportRequest reportRequest,
            UserInfo reporter,
            ReportReason reason
    ) {
        return new Report(
                reporter,
                reportRequest.targetId(),
                reportRequest.reportType(),
                reportRequest.details(),
                reason
        );
    }
}
