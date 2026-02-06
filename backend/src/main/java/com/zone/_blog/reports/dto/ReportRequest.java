package com.zone._blog.reports.dto;

import java.util.UUID;

import com.zone._blog.reports.ReportType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportRequest(
        @NotNull(message = "report must have a target id either a USER or a POST")
        UUID targetId,
        @NotNull(message = "report must have a target target")
        String targetName,
        @NotBlank(message = "Report must have a valid reason")
        @Size(min = 10, max = 500)
        String details,
        @NotNull(message = "report must have a report type either a USER or a POST")
        ReportType reportType,
        @NotNull(message = "report must have a report reason")
        UUID reason
        ) {

}
