package com.zone._blog.moderation;

import java.util.List;
import java.util.UUID;

import com.zone._blog.moderation.dto.PostByPeriod;
import com.zone._blog.moderation.dto.PostByUser;
import com.zone._blog.moderation.dto.ReportByPeriod;
import com.zone._blog.moderation.dto.ReportByResolved;
import com.zone._blog.moderation.dto.ReportByType;
import com.zone._blog.moderation.dto.ReportByUser;
import com.zone._blog.reports.ReportStatus;
import com.zone._blog.reports.dto.ReportResponse;

public interface ModerationService {

    public List<ReportResponse> getReports();

    public ReportResponse getReport(UUID id);

    public ReportResponse updateReport(ReportStatus reportStatus, UUID reportId);

    public void deleteReport(UUID id);

    public Long getReportCount();

    public List<ReportByPeriod> getReportCountByPeriod(Period period);

    public List<ReportByType> getReportCountType();

    public List<ReportByUser> getMostReportedUsers();

    public ReportByResolved getResolvedReportsCount();

    public List<PostByPeriod> getPostCountByPeriod(Period period);

    public List<PostByUser> getMostPostingUsers();

    public Long getPostsCount();

    public Long getUsersCount();

}
