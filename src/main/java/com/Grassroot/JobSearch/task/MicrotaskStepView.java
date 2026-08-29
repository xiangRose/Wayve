package com.Grassroot.JobSearch.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MicrotaskStepView {

    private MicrotaskStepView() {}

  @SuppressWarnings("unchecked")
    public static Map<String, Object> publicStep(Map<String, Object> step, int stepNum, int total) {
        Map<String, Object> out = new HashMap<>();
        out.put("step", stepNum);
        out.put("totalSteps", total);
        out.put("time", step.get("time"));
        out.put("speaker", step.get("speaker"));
        out.put("speakerRole", step.get("speakerRole"));
        out.put("message", step.get("message"));
        out.put("prompt", step.get("prompt"));
        out.put("options", publicOptions(step));
        return out;
    }

  @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> publicOptions(Map<String, Object> step) {
        Object optionsObj = step.get("options");
        if (optionsObj == null) {
            return List.of();
        }
        List<Map<String, Object>> options = (List<Map<String, Object>>) optionsObj;
        List<Map<String, Object>> publicList = new ArrayList<>();
        for (Map<String, Object> option : options) {
            Map<String, Object> pub = new HashMap<>();
            pub.put("id", option.get("id"));
            String label = String.valueOf(option.get("label"));
            pub.put("label", label.startsWith(option.get("id") + ".") ? label : option.get("id") + ". " + label);
            publicList.add(pub);
        }
        return publicList;
    }
}
