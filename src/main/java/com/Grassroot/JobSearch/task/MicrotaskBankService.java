package com.Grassroot.JobSearch.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.Grassroot.JobSearch.common.JsonResourceLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class MicrotaskBankService {

    private static final List<String> SET_IDS = List.of("A", "B", "C", "D");
    private static final Map<Integer, Integer> SCORE_TO_RADAR = Map.of(2, 40, 3, 60, 4, 80, 5, 100);

    private final JsonResourceLoader json;
    private final Random random = new Random();

    public MicrotaskBankService(JsonResourceLoader json) {
        this.json = json;
    }

    public String pickRandomSetId() {
        return SET_IDS.get(random.nextInt(SET_IDS.size()));
    }

    public Map<String, Object> buildTemplate(String jobId, String setId) {
        Map<String, Object> set = requireSet(jobId, setId);
        List<Map<String, Object>> questions = castList(set.get("questions"));
        List<Map<String, Object>> steps = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Map<String, Object> q = questions.get(i);
            Map<String, Object> step = new HashMap<>();
            step.put("stepNumber", i + 1);
            step.put("stepTitle", "微任务 " + (i + 1));
            step.put("stepType", "microtask_choice");
            step.put("time", q.get("time"));
            step.put("speaker", q.get("speaker"));
            step.put("speakerRole", q.get("speakerRole"));
            step.put("message", q.get("message"));
            step.put("prompt", q.get("prompt"));
            step.put("dimension", q.get("dimension"));
            step.put("options", q.get("options"));
            step.put("decision", Map.of(
                    "type", "single_choice",
                    "prompt", q.get("prompt"),
                    "options", q.get("options")));
            steps.add(step);
        }
        Map<String, Object> template = new HashMap<>();
        template.put("jobId", jobId);
        template.put("scaffoldType", "career_changer");
        template.put("setId", setId);
        template.put("title", set.get("theme"));
        template.put("scenario", set.get("theme"));
        template.put("steps", steps);
        return template;
    }

    public int resolveRawScore(Map<String, Object> step, String optionId) {
        if (optionId == null || optionId.isBlank()) {
            return 0;
        }
        Object decision = step.get("decision");
        if (decision instanceof Map<?, ?> decisionMap) {
            return scoreFromOptions(decisionMap.get("options"), optionId);
        }
        return scoreFromOptions(step.get("options"), optionId);
    }

    public int toRadarScore(int rawScore) {
        return SCORE_TO_RADAR.getOrDefault(rawScore, 0);
    }

    public Map<String, Object> buildRadarFromSteps(List<Map<String, Object>> steps, List<Map<String, Object>> stepDefs) {
        List<Map<String, Object>> dimensions = new ArrayList<>();
        for (int i = 0; i < steps.size() && i < stepDefs.size(); i++) {
            Map<String, Object> row = steps.get(i);
            Map<String, Object> def = stepDefs.get(i);
            Object radar = row.get("radarScore");
            int radarScore = radar instanceof Number n ? n.intValue() : 0;
            if (radarScore <= 0) {
                String optionId = extractOptionId(row.get("answer"));
                int raw = resolveRawScore(def, optionId);
                radarScore = toRadarScore(raw);
            }
            dimensions.add(Map.of(
                    "name", String.valueOf(def.get("dimension")),
                    "score", radarScore,
                    "rawScore", row.getOrDefault("rawScore", 0)));
        }
        Map<String, Object> radar = new HashMap<>();
        radar.put("dimensions", dimensions);
        radar.put("labels", dimensions.stream().map(d -> d.get("name")).toList());
        radar.put("scores", dimensions.stream().map(d -> d.get("score")).toList());
        return radar;
    }

  @SuppressWarnings("unchecked")
    private Map<String, Object> requireSet(String jobId, String setId) {
        Map<String, Object> root = json.load("seed/microtask-bank.json", new TypeReference<>() {});
        Map<String, Object> jobs = (Map<String, Object>) root.get("jobs");
        Map<String, Object> job = (Map<String, Object>) jobs.get(jobId);
        if (job == null) {
            throw new IllegalStateException("微任务题库缺少岗位: " + jobId);
        }
        Map<String, Object> sets = (Map<String, Object>) job.get("sets");
        Map<String, Object> set = (Map<String, Object>) sets.get(setId);
        if (set == null) {
            throw new IllegalStateException("微任务题库缺少套题: " + jobId + " / " + setId);
        }
        return set;
    }

    private int scoreFromOptions(Object optionsObj, String optionId) {
        if (optionsObj instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof Map<?, ?> option) {
                    if (String.valueOf(option.get("id")).equals(optionId)) {
                        Object score = option.get("score");
                        return score instanceof Number n ? n.intValue() : 0;
                    }
                }
            }
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    private String extractOptionId(Object answerObj) {
        if (answerObj instanceof Map<?, ?> answer) {
            Object id = answer.get("selectedOptionId");
            return id == null ? "" : String.valueOf(id);
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castList(Object value) {
        if (value instanceof List<?> list) {
            List<Map<String, Object>> out = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Map<?, ?> map) {
                    out.add((Map<String, Object>) map);
                }
            }
            return out;
        }
        return List.of();
    }
}
