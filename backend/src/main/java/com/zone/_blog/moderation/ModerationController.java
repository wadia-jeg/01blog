package com.zone._blog.moderation;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.moderation.dto.PostByPeriod;
import com.zone._blog.moderation.dto.PostByUser;
import com.zone._blog.moderation.dto.ReportByPeriod;
import com.zone._blog.moderation.dto.ReportByResolved;
import com.zone._blog.moderation.dto.ReportByType;
import com.zone._blog.moderation.dto.ReportByUser;
import com.zone._blog.reports.ReportStatus;
import com.zone._blog.reports.dto.ReportResponse;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("${app.api.v1}/admin")
@PreAuthorize("hasRole('ADMIN')")
public class ModerationController {

    private final ModerationService moderationService;

    public ModerationController(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportResponse>> getReports() {
        return ResponseEntity.ok(this.moderationService.getReports());
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<List<ReportResponse>> getReport(@PathVariable UUID id) {
        return ResponseEntity.ok(this.moderationService.getReports());
    }

    @PatchMapping("/reports/{id}")
    public ResponseEntity<ReportResponse> updateReportStatus(@NotNull @RequestBody ReportStatus reportStatus, @PathVariable UUID id) {
        return ResponseEntity.ok(this.moderationService.updateReport(reportStatus, id));
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable UUID id) {
        this.moderationService.deleteReport(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Report deleted succefully");
    }

    @GetMapping("/reports/count")
    public ResponseEntity<Long> getReportCount() {
        return ResponseEntity.ok(this.moderationService.getReportCount());
    }

    @GetMapping("/reports/count/period/{period}")
    public ResponseEntity<List<ReportByPeriod>> getReportCountByPeriod(
            @PathVariable Period period) {
        return ResponseEntity.ok(this.moderationService.getReportCountByPeriod(period));
    }

    @GetMapping("/reports/count/type")
    public ResponseEntity<List<ReportByType>> getReportCountByType() {
        return ResponseEntity.ok(this.moderationService.getReportCountType());
    }

    @GetMapping("/reports/count/users")
    public ResponseEntity<List<ReportByUser>> getReportCountByUsers() {
        return ResponseEntity.ok(this.moderationService.getMostReportedUsers());
    }

    @GetMapping("/reports/count/resolved")
    public ResponseEntity<ReportByResolved> getResolvedReportsCount() {
        return ResponseEntity.ok(this.moderationService.getResolvedReportsCount());
    }

    @GetMapping("/posts/count")
    public ResponseEntity<Long> getPostCount() {
        return ResponseEntity.ok(this.moderationService.getPostsCount());
    }

    @GetMapping("/posts/count/period/{period}")
    public ResponseEntity<List<PostByPeriod>> getPostCountByPeriod(
            @PathVariable Period period) {
        return ResponseEntity.ok(this.moderationService.getPostCountByPeriod(period));
    }

    @GetMapping("/posts/count/users")
    public ResponseEntity<List<PostByUser>> getPostCountByUsers() {
        return ResponseEntity.ok(this.moderationService.getMostPostingUsers());
    }

}
