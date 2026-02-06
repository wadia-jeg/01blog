package com.zone._blog.reports;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.zone._blog.exceptions.BadRequestException;
import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.exceptions.UnauthorizedException;
import com.zone._blog.reports.dto.ReportRequest;
import com.zone._blog.reports.dto.ReportResponse;
import com.zone._blog.reports.reason.ReportReason;
import com.zone._blog.reports.reason.ReportReasonRepository;
import com.zone._blog.users.UserInfo;
import com.zone._blog.users.UserService;

import jakarta.transaction.Transactional;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportReasonRepository reportReasonRepository;
    private final UserService userService;

    public ReportServiceImpl(
            ReportRepository reportRepository,
            ReportReasonRepository reportReasonRepository,
            UserService userService
    ) {
        this.reportRepository = reportRepository;
        this.reportReasonRepository = reportReasonRepository;
        this.userService = userService;
    }

    @Override
    public List<ReportResponse> getReports() {
        UserInfo currentUser = this.userService.getCurrentUser();

        List<ReportResponse> reports = this.reportRepository.findReportsByReporterId(currentUser.getId());

        return reports;
    }

    @Override
    public ReportResponse getReport(UUID id) {
        UserInfo currentUser = this.userService.getCurrentUser();

        ReportResponse report = this.reportRepository.findReportById(id).orElseThrow(
                () -> new ResourceNotFoundException("Report Not Found")
        );

        if (!report.reporter().equals(currentUser.getId())) {
            throw new UnauthorizedException("Unauthorized Access");
        }

        return report;
    }

    @Transactional
    @Override
    public ReportResponse addReport(ReportRequest reportRequest) {
        UserInfo currentUser = this.userService.getCurrentUser();

        this.checkReportRequest(reportRequest);

        if (this.reportRepository.existsByReporterIdAndTargetIdAndReportTypeAndStatus(
                currentUser.getId(), reportRequest.targetId(), reportRequest.reportType(), ReportStatus.PENDING)) {
            throw new BadRequestException("You Already reported this user");
        }

        ReportReason reason = this.reportReasonRepository.findById(reportRequest.reason()).orElseThrow(() -> new ResourceNotFoundException("This type of reason not found"));

        Report reportEntity = ReportMapper.toReport(reportRequest, currentUser, reason);

        this.reportRepository.save(reportEntity);

        return ReportMapper.toReportResponse(reportEntity);

    }

    @Transactional
    @Override
    public ReportResponse updateReport(ReportStatus reportStatus, UUID reportId) {
        UserInfo currentUser = this.userService.getCurrentUser();

        Report reportEntity = this.reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (!reportEntity.getStatus().equals(ReportStatus.PENDING)) {
            throw new BadRequestException("Report is already resolved or canceled");
        }

        if (reportStatus != ReportStatus.CANCELED) {
            throw new BadRequestException("Only cancellation is allowed");
        }

        this.checkAuthorizedAccess(currentUser, reportEntity);
        reportEntity.setStatus(ReportStatus.CANCELED);

        this.reportRepository.save(reportEntity);

        return ReportMapper.toReportResponse(reportEntity);

    }

    @Override
    public void deleteReport(UUID reportId) {
        UserInfo currentUser = this.userService.getCurrentUser();

        Report reportEntity = this.reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        this.checkAuthorizedAccess(currentUser, reportEntity);

        this.reportRepository.delete(reportEntity);

    }

    public void checkAuthorizedAccess(UserInfo currentUser, Report reportEntity) {
        if (!reportEntity.getReporter().equals(currentUser)) {
            throw new UnauthorizedException("You are not authorised to update this content");
        }

    }

    public void checkReportRequest(ReportRequest reportRequest) {
        UserInfo currentUser = this.userService.getCurrentUser();

        if (reportRequest.targetId() == null) {
            throw new BadRequestException("You must specify the reported target, USER or POST");
        }

        if (reportRequest.reportType().equals(ReportType.USER)
                && reportRequest.targetId().equals(currentUser.getId())) {
            throw new BadRequestException("You can't report yourself");
        }

    }

}
