package com.Grassroot.JobSearch.job;

import com.Grassroot.JobSearch.ai.AiOrchestrator;
import com.Grassroot.JobSearch.ai.DemoDataService;
import com.Grassroot.JobSearch.session.SessionService;
import com.Grassroot.JobSearch.session.UserSession;
import com.Grassroot.JobSearch.common.ApiException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final DemoDataService demoDataService;
    private final SessionService sessionService;
    private final AiOrchestrator aiOrchestrator;

    public JobService(
            JobRepository jobRepository,
            DemoDataService demoDataService,
            SessionService sessionService,
            AiOrchestrator aiOrchestrator) {
        this.jobRepository = jobRepository;
        this.demoDataService = demoDataService;
        this.sessionService = sessionService;
        this.aiOrchestrator = aiOrchestrator;
    }

    public Map<String, Object> listAll() {
        return Map.of("jobs", jobRepository.findAll().stream().map(this::toCard).toList());
    }

    public Map<String, Object> recommend(boolean demoMode, String sessionId, List<String> rejectedIds) {
        if (demoMode) {
            return Map.of("recommendations", demoDataService.jobRecommendations());
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "SESSION_REQUIRED", "需要 X-Session-Id");
        }
        Set<String> rejected = rejectedIds == null ? Set.of() : Set.copyOf(rejectedIds);
        UserSession session = sessionService.find(sessionId);
        Map<String, Object> context = new HashMap<>();
        context.put("user_stage", session.getUserStage() == null ? "beginner" : session.getUserStage().name());
        context.put("clarity_level", session.getClarityLevel() == null ? "unknown" : session.getClarityLevel().name());
        context.put("work_preference", session.getWorkPreference());
        context.put("background_text", session.getBackgroundText());
        context.put("team_role_description", session.getTeamRoleDescription());
        context.put("resume_text", session.getResumeText());
        context.put("rejected_job_ids", rejected);

        List<Map<String, Object>> recommendations = aiOrchestrator.recommendJobs(context).stream()
                .filter(r -> !rejected.contains(String.valueOf(r.get("jobId"))))
                .limit(3)
                .toList();

        if (recommendations.isEmpty()) {
            recommendations = jobRepository.findAll().stream()
                    .filter(j -> !rejected.contains(j.getJobId()))
                    .limit(3)
                    .map(j -> Map.<String, Object>of(
                            "jobId", j.getJobId(),
                            "name", j.getName(),
                            "reason", "值得先体验：" + j.getWhyExperience()))
                    .toList();
        }
        return Map.of("recommendations", recommendations);
    }

    private Map<String, Object> toCard(JobModel job) {
        Map<String, Object> m = new HashMap<>();
        m.put("jobId", job.getJobId());
        m.put("name", job.getName());
        m.put("definition", job.getDefinition());
        m.put("coreWorkObject", job.getCoreWorkObject());
        m.put("typicalWorkSnippet", job.getTypicalWorkSnippet());
        m.put("whyExperience", job.getWhyExperience());
        m.put("estimatedMinutes", job.getEstimatedMinutes());
        m.put("taskStatus", job.getTaskStatus());
        m.put("competencyRequirements", job.getCompetencyRequirements());
        m.put("specificCompetencies", job.getSpecificCompetencies());
        return m;
    }
}
