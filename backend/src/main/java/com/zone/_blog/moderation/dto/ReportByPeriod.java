package com.zone._blog.moderation.dto;

public record ReportByPeriod(
        Integer period,
        Integer year,
        Long count
        ) {

}
