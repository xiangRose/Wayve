package com.Grassroot.JobSearch.job;

import com.Grassroot.JobSearch.ai.DemoDataService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final DemoDataService demoDataService;

    public JobService(JobRepository jobRepository, DemoDataService demoDataService) {
        this.jobRepository = jobRepository;
        this.demoDataService = demoDataService;
    }

    public Map<String, Object> listAll() {
        return Map.of("jobs", jobRepository.findAll().stream().map(this::toCard).toList());
    }

    public Map<String, Object> recommend(boolean demoMode, List<String> rejectedIds) {
        if (demoMode) {
            return Map.of("recommendations", demoDataService.jobRecommendations());
        }
        Set<String> rejected = rejectedIds == null ? Set.of() : Set.copyOf(rejectedIds);
        List<Map<String, Object>> list = jobRepository.findAll().stream()
                .filter(j -> !rejected.contains(j.getJobId()))
                .limit(3)
                .map(j -> Map.<String, Object>of(
                        "jobId", j.getJobId(),
                        "name", j.getName(),
                        "reason", "该岗位适合体验" + j.getWhyExperience()))
                .toList();
        return Map.of("recommendations", list);
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
