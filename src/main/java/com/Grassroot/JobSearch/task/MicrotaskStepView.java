package com.Grassroot.JobSearch.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class MicrotaskStepView {

    private MicrotaskStepView() {}

    @SuppressWarnings("unchecked")
    static Map<String, Object> publicStep(Map<String, Object> step, int stepNum, int totalSteps) {
        Map<String, Object> view = new HashMap<>();
        view.put("step", stepNum);
        view.put("totalSteps", totalSteps);
        view.put("stepTitle", step.get("stepTitle"));
        view.put("stepType", step.get("stepType"));
        view.put("time", step.get("time"));
        view.put("speaker", step.get("speaker"));
        view.put("speakerRole", step.get("speakerRole"));
        view.put("message", step.get("message"));
        view.put("prompt", step.get("prompt"));

        List<Map<String, Object>> options = new ArrayList<>();
        Object decision = step.get("decision");
        if (decision instanceof Map<?, ?> decisionMap) {
            Object rawOptions = decisionMap.get("options");
            if (rawOptions instanceof List<?> list) {
                for (Object item : list) {
                    if (item instanceof Map<?, ?> option) {
                        String id = String.valueOf(option.get("id"));
                        String label = String.valueOf(option.get("label"));
                        options.add(Map.of("id", id, "label", formatLabel(id, label)));
                    }
                }
            }
        }
        view.put("options", options);
        return view;
    }

    private static String formatLabel(String id, String label) {
        if (label == null || label.isBlank()) {
            return id;
        }
        String trimmed = label.trim();
        if (trimmed.startsWith(id + ".") || trimmed.startsWith(id + "．")) {
            return trimmed;
        }
        return id + ". " + trimmed;
    }
}
