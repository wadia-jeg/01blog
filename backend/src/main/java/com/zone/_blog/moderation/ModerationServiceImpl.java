package com.zone._blog.moderation;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.zone._blog.exceptions.BadRequestException;
import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.moderation.dto.PostByPeriod;
import com.zone._blog.moderation.dto.PostByUser;
import com.zone._blog.moderation.dto.ReportByPeriod;
import com.zone._blog.moderation.dto.ReportByResolved;
import com.zone._blog.moderation.dto.ReportByType;
import com.zone._blog.moderation.dto.ReportByUser;
import com.zone._blog.reports.Report;
import com.zone._blog.reports.ReportMapper;
import com.zone._blog.reports.ReportRepository;
import com.zone._blog.reports.ReportStatus;
import com.zone._blog.reports.dto.ReportResponse;
import com.zone._blog.users.UserService;

import jakarta.transaction.Transactional;

@Service
@PreAuthorize("hasRole('ADMIN')")
public class ModerationServiceImpl implements ModerationService {

    private final ReportRepository reportRepository;
    private final ReportAggregateRepository reportAggregateRepository;
    private final PostAggregateRepository postAggregateRepository;
    private final UserAggregateRepository userAggregateRepository;

    public ModerationServiceImpl(
            ReportRepository reportRepository,
            ReportAggregateRepository reportAggregateRepository,
            PostAggregateRepository postAggregateRepository,
            UserAggregateRepository userAggregateRepository,
            UserService userService
    ) {
        this.reportRepository = reportRepository;
        this.reportAggregateRepository = reportAggregateRepository;
        this.postAggregateRepository = postAggregateRepository;
        this.userAggregateRepository = userAggregateRepository;

    }

    @Override
    public List<ReportResponse> getReports() {

        return this.reportRepository.findAllReports();
    }

    @Override
    public ReportResponse getReport(UUID id) {
        return this.reportRepository.findReportById(id).orElseThrow(
                () -> new ResourceNotFoundException("Report Not Found")
        );
    }

    @Transactional
    @Override
    public ReportResponse updateReport(ReportStatus reportStatus, UUID reportId) {

        Report reportEntity = this.reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        if (!reportEntity.getStatus().equals(ReportStatus.PENDING)) {
            throw new BadRequestException("Report is already resolved or canceled");
        }

        if (reportStatus == ReportStatus.RESOLVED) {
            reportEntity.setStatus(ReportStatus.RESOLVED);
            reportEntity.setResolvedAt(Instant.now());
        }

        this.reportRepository.save(reportEntity);

        return ReportMapper.toReportResponse(reportEntity);
    }

    @Override
    public void deleteReport(UUID reportId) {

        Report reportEntity = this.reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found"));

        this.reportRepository.delete(reportEntity);

    }

    @Override
    public Long getReportCount() {
        return this.reportAggregateRepository.count();
    }

    @Override
    public List<ReportByPeriod> getReportCountByPeriod(Period period) {
        return switch (period) {
            case WEEK ->
                this.reportAggregateRepository.countReportsByWeek();
            case MONTH ->
                this.reportAggregateRepository.countReportsByMonth();
            case YEAR ->
                this.reportAggregateRepository.countReportsByYear();
        };
    }

    @Override
    public List<ReportByType> getReportCountType() {
        return this.reportAggregateRepository.countReportsByType();
    }

    @Override
    public List<ReportByUser> getMostReportedUsers() {
        return this.reportAggregateRepository.countReportsByUser();
    }

    @Override
    public ReportByResolved getResolvedReportsCount() {
        return this.reportAggregateRepository.countReportByResolved();
    }

    @Override
    public List<PostByPeriod> getPostCountByPeriod(Period period) {
        return switch (period) {
            case WEEK ->
                this.postAggregateRepository.countPostsByWeek();
            case MONTH ->
                this.postAggregateRepository.countPostsByMonth();
            case YEAR ->
                this.postAggregateRepository.countPostsByYear();
        };
    }

    @Override
    public List<PostByUser> getMostPostingUsers() {
        return this.postAggregateRepository.countPostsByUser();

    }

    @Override
    public Long getPostsCount() {
        return this.postAggregateRepository.count();
    }

    @Override
    public Long getUsersCount() {
        return this.userAggregateRepository.count();
    }

}
