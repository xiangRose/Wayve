package com.Grassroot.JobSearch.report;

import com.Grassroot.JobSearch.config.AppConstants;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;
    private final String demoSessionId;

    public ReportController(ReportService reportService, @Value("${app.demo-session-id}") String demoSessionId) {
        this.reportService = reportService;
        this.demoSessionId = demoSessionId;
    }

    @PostMapping("/generate")
    public Map<String, Object> generate(
            @RequestHeader(value = AppConstants.HEADER_SESSION_ID, required = false) String sessionId,
            @RequestHeader(value = AppConstants.HEADER_DEMO_MODE, required = false) String demoMode,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String jobId,
            @org.springframework.web.bind.annotation.RequestBody(required = false) ReportGenerateRequest body) {
        boolean isDemo = "true".equalsIgnoreCase(demoMode) || demoSessionId.equals(sessionId);
        return reportService.generate(sessionId, isDemo, jobId, body);
    }

    @GetMapping("/{reportId}")
    public Map<String, Object> get(@PathVariable String reportId) {
        return reportService.get(reportId);
    }

    @PostMapping("/{reportId}/target")
    public Map<String, Object> target(@PathVariable String reportId, @Valid @RequestBody TargetSelectRequest req) {
        return reportService.selectTarget(reportId, req.targetJobId());
    }
}
