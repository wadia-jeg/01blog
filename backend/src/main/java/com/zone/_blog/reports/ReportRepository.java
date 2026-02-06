package com.zone._blog.reports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zone._blog.reports.dto.ReportResponse;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    boolean existsByReporterIdAndTargetIdAndReportTypeAndStatus(
            UUID reporterId,
            UUID targetId,
            ReportType reportType,
            ReportStatus reportStatus
    );

    @Query(
            """
            SELECT new com.zone._blog.reports.dto.ReportResponse(
                r.id,
                r.reporter.id,
                r.targetId,
                r.targetName,
                r.reportType, 
                r.details,
                r.reason,
                r.reportedAt,
                r.resolvedAt,
                r.status
            )
            FROM Report r
            ORDER BY r.reportedAt DESC
    """
    )

    List<ReportResponse> findAllReports();

    @Query(
            """
            SELECT new com.zone._blog.reports.dto.ReportResponse(
                r.id,
                r.reporter.id,
                r.targetId,
                r.targetName,
                r.reportType, 
                r.details,
                r.reason,
                r.reportedAt,
                r.resolvedAt,
                r.status
            )
            FROM Report r
            WHERE (:reporterId IS NULL OR r.reporter.id = :reporterId)
            ORDER BY r.reportedAt DESC
    """
    )
    List<ReportResponse> findReportsByReporterId(UUID reporterId);

    @Query(
            """
            SELECT new com.zone._blog.reports.dto.ReportResponse(
                r.id,
                r.reporter.id,
                r.targetId,
                r.targetName,
                r.reportType, 
                r.details,
                r.reason,
                r.reportedAt,
                r.resolvedAt,
                r.status
            )
            FROM Report r
            WHERE r.id = :reporteId
            ORDER BY r.reportedAt DESC
    """
    )
    Optional<ReportResponse> findReportById(@Param("reportId") UUID reportId);

}


/*
@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    boolean existsByReporterIdAndTargetIdAndReportTypeAndStatus(
            UUID reporterId,
            UUID targetId,
            ReportType reportType,
            ReportStatus reportStatus
    );

    // @Query(
    //         """
    //         SELECT new com.zone._blog.reports.dto.ReportResponse(
    //             r.id,
    //             r.reporter.id,
    //             r.targetId,
    //             CASE
    //                 WHEN r.reportType = com.zone._blog.reports.ReportType.USER THEN u.username
    //                 WHEN r.reportType = com.zone._blog.reports.ReportType.POST THEN p.title
    //                 ELSE ''
    //             END,
    //             r.reportType, 
    //             r.details,
    //             r.reason.type,
    //             r.reportedAt,
    //             r.resolvedAt,
    //             r.status
    //         )
    //         FROM Report r
    //         LEFT JOIN UserInfo u ON r.targetId = u.id 
    //         LEFT JOIN Post p ON r.targetId = p.id 
    //         WHERE (:reporterId IS NULL OR r.reporter.id = :reporterId)
    //         ORDER BY r.reportedAt DESC
    // """
    // )
    List<ReportResponse> findAllReports();

    List<ReportResponse> findReportsByReporterId(UUID reporterId);

    // @Query(
    //         """
    //         SELECT new com.zone._blog.reports.dto.ReportResponse(
    //             r.id,
    //             r.reporter.id,
    //             r.targetId,
    //             CASE
    //                 WHEN r.reportType = com.zone._blog.reports.ReportType.USER THEN u.username
    //                 WHEN r.reportType = com.zone._blog.reports.ReportType.POST THEN p.title
    //                 ELSE ''
    //             END,
    //             r.reportType, 
    //             r.details,
    //             r.reason.type,
    //             r.reportedAt,
    //             r.resolvedAt,
    //             r.status
    //         )
    //         FROM Report r
    //         LEFT JOIN UserInfo  u ON r.targetId = u.id 
    //         LEFT JOIN Post  p ON r.targetId = p.id 
    //         WHERE r.id = :reportId
    // """
    // )
    Optional<ReportResponse> findByReportId(UUID reportId);

}


 */
