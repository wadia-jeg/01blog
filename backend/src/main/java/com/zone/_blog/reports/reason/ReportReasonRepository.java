package com.zone._blog.reports.reason;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportReasonRepository extends JpaRepository<ReportReason, UUID> {

    boolean existsByType(String type);
}
