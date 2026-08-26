package com.Grassroot.JobSearch.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.Grassroot.JobSearch.common.JsonResourceLoader;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DemoDataService {

    private final Map<String, Object> demoSession;

    public DemoDataService(JsonResourceLoader loader) {
        this.demoSession = loader.load("seed/demo-session.json", new TypeReference<>() {});
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> jobRecommendations() {
        return (List<Map<String, Object>>) demoSession.get("jobRecommendations");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> report() {
        return (Map<String, Object>) demoSession.get("report");
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> interestSignals() {
        return (List<Map<String, Object>>) demoSession.get("interestSignals");
    }

    public String selectedTargetJob() {
        return (String) demoSession.get("selectedTargetJob");
    }
}
