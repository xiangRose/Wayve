package com.Grassroot.JobSearch.job;

import com.Grassroot.JobSearch.config.AppConstants;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;
    private final String demoSessionId;

    public JobController(JobService jobService, @Value("${app.demo-session-id}") String demoSessionId) {
        this.jobService = jobService;
        this.demoSessionId = demoSessionId;
    }

    @GetMapping
    public Map<String, Object> list() {
        return jobService.listAll();
    }

    @PostMapping("/recommend")
    public Map<String, Object> recommend(
            @RequestHeader(value = AppConstants.HEADER_SESSION_ID, required = false) String sessionId,
            @RequestHeader(value = AppConstants.HEADER_DEMO_MODE, required = false) String demoMode,
            @RequestBody(required = false) Map<String, List<String>> body) {
        boolean isDemo = "true".equalsIgnoreCase(demoMode) || demoSessionId.equals(sessionId);
        List<String> rejected = body == null ? List.of() : body.getOrDefault("rejectedJobIds", List.of());
        return jobService.recommend(isDemo, sessionId, rejected);
    }
}
