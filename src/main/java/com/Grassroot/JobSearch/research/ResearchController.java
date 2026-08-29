package com.Grassroot.JobSearch.research;

import com.Grassroot.JobSearch.ai.JobResearchService;
import com.Grassroot.JobSearch.llm.LlmClient;
import com.Grassroot.JobSearch.llm.LlmProperties;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/research")
public class ResearchController {

    private final JobResearchService jobResearchService;
    private final LlmClient llmClient;
    private final LlmProperties llmProperties;

    public ResearchController(
            JobResearchService jobResearchService, LlmClient llmClient, LlmProperties llmProperties) {
        this.jobResearchService = jobResearchService;
        this.llmClient = llmClient;
        this.llmProperties = llmProperties;
    }

    @GetMapping("/ai/status")
    public Map<String, Object> aiStatus() {
        return Map.of(
                "enabled", llmProperties.isEnabled(),
                "configured", llmClient.isReady(),
                "baseUrl", llmProperties.getBaseUrl(),
                "model", llmProperties.getModelPro());
    }

    @PostMapping("/jobs/{jobId}/aggregate")
    public Map<String, Object> aggregate(
            @PathVariable String jobId, @RequestParam(defaultValue = "false") boolean dryRun) {
        return jobResearchService.aggregateJob(jobId, dryRun);
    }
}
