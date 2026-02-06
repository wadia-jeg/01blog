package com.zone._blog.config.startup;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.zone._blog.reports.reason.ReportReason;
import com.zone._blog.reports.reason.ReportReasonRepository;
import com.zone._blog.reports.reason.ReportReasonType;

@Component
public class InitReportReasons {

    private final ReportReasonRepository reportReasonRepository;

    public InitReportReasons(ReportReasonRepository reportReasonRepository) {
        this.reportReasonRepository = reportReasonRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initReasons() {
        ReportReasonType[] reasons = ReportReasonType.values();

        for (ReportReasonType reason : reasons) {
            String reasonStr = reason.name();
            if (!this.reportReasonRepository.existsByType(reasonStr)) {
                this.reportReasonRepository.save(new ReportReason(reasonStr));
            }
        }
    }

}
