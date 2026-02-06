package com.zone._blog.moderation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zone._blog.reports.Report;
import com.zone._blog.moderation.dto.ReportByPeriod;
import com.zone._blog.moderation.dto.ReportByResolved;
import com.zone._blog.moderation.dto.ReportByType;
import com.zone._blog.moderation.dto.ReportByUser;

@Repository
public interface ReportAggregateRepository extends JpaRepository<Report, UUID> {

    @Query(
            """
        SELECT r.reason.type AS type, 
               COUNT(r) AS count
        FROM Report r
        GROUP BY r.reason.type
        ORDER BY COUNT(r) DESC
        """
    )
    List<ReportByType> countReportsByType();

    @Query(
            """
            SELECT COUNT(r) AS count,
                FUNCTION('year', r.reportedAt) AS year,
                FUNCTION('week', r.reportedAt) AS period
            FROM Report r
                GROUP BY 
                    FUNCTION('week', r.reportedAt),
                    FUNCTION('year', r.reportedAt)
                ORDER BY 
                    FUNCTION('year', r.reportedAt) DESC,
                    FUNCTION('week', r.reportedAt) DESC
        """
    )
    List<ReportByPeriod> countReportsByWeek();

    @Query(
            """
            SELECT COUNT(r) AS count,
                FUNCTION('year', r.reportedAt) AS year,
                FUNCTION('month', r.reportedAt) AS period
            FROM Report r
                GROUP BY 
                    FUNCTION('month', r.reportedAt),
                    FUNCTION('year', r.reportedAt)
                ORDER BY 
                    FUNCTION('year', r.reportedAt) DESC,
                    FUNCTION('month', r.reportedAt) DESC
        """
    )
    List<ReportByPeriod> countReportsByMonth();

    @Query(
            """
            SELECT COUNT(r) AS count,
                FUNCTION('year', r.reportedAt) AS year,
                NULL AS period
            FROM Report r
                GROUP BY 
                    FUNCTION('year', r.reportedAt)
                ORDER BY 
                    FUNCTION('year', r.reportedAt) DESC
        """
    )
    List<ReportByPeriod> countReportsByYear();

    @Query(
            """
           SELECT u.username AS username, 
                  COUNT(r) AS count
           FROM Report r
           JOIN UserInfo u ON r.targetId = u.id
           WHERE r.reportType = com.zone._blog.reports.ReportType.USER
           GROUP BY u.username
           ORDER BY COUNT(r) DESC
        """
    )
    List<ReportByUser> countReportsByUser();

    @Query("""
    SELECT COUNT(r),
           SUM( CASE WHEN r.resolvedAt IS NOT NULL THEN 1 ELSE 0 END )AS total 
    FROM Report r
""")
    ReportByResolved countReportByResolved();

}
