package com.Grassroot.JobSearch.report;

import java.util.List;
import java.util.Map;

public record ReportGenerateRequest(
        List<Map<String, Object>> microtaskChoiceSignals,
        String setId) {
}
