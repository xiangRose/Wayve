package com.Grassroot.JobSearch.task;

import java.util.List;
import java.util.Map;

public record StepSubmitRequest(Map<String, Object> answer, List<Map<String, Object>> events) {
}
