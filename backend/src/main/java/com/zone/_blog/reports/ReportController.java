package com.zone._blog.reports;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.reports.dto.ReportRequest;
import com.zone._blog.reports.dto.ReportResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("${app.api.v1}/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService
    ) {
        this.reportService = reportService;
    }

    @GetMapping("")
    public ResponseEntity<List<ReportResponse>> getReports() {
        List<ReportResponse> reports = this.reportService.getReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable UUID id) {
        ReportResponse report = this.reportService.getReport(id);
        return ResponseEntity.ok(report);
    }

    @PostMapping("")
    public ResponseEntity<ReportResponse> addReport(@Valid @RequestBody ReportRequest reporterRequest) {
        ReportResponse report = this.reportService.addReport(reporterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReportResponse> updateReport(@PathVariable UUID id,
            @NotNull @RequestBody ReportStatus reportStatus) {
        ReportResponse report = this.reportService.updateReport(reportStatus, id);
        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable UUID id) {
        this.reportService.deleteReport(id);
        return ResponseEntity.ok("Report deleted succefully");
    }

}
