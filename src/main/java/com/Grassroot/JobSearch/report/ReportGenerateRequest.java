package com.Grassroot.JobSearch.report;

import java.util.List;
import java.util.Map;

public record ReportGenerateRequest(
        List<Map<String, Object>> microtaskChoiceSignals,
        List<Map<String, Object>> userSubjectiveHighlights,
        List<Map<String, Object>> microtaskCapabilitySummary,
        String setId) {}
