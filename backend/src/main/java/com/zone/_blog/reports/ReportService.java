package com.zone._blog.reports;

import java.util.List;
import java.util.UUID;

import com.zone._blog.reports.dto.ReportRequest;
import com.zone._blog.reports.dto.ReportResponse;

public interface ReportService {

    public List<ReportResponse> getReports();

    public ReportResponse getReport(UUID id);

    public ReportResponse addReport(ReportRequest reportRequest);

    public ReportResponse updateReport(ReportStatus reportStatus, UUID report);

    public void deleteReport(UUID report);

}
