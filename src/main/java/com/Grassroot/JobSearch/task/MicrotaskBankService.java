package com.Grassroot.JobSearch.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.Grassroot.JobSearch.common.JsonResourceLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

@Service
public class MicrotaskBankService {

    private static final List<String> SET_IDS = List.of("A", "B", "C", "D");
    private static final int QUESTIONS_PER_SET = 6;
    private static final Map<Integer, Integer> RADAR_SCORE_MAP = Map.of(
            5, 100,
            4, 80,
            3, 60,
            2, 40,
            1, 20,
            0, 0
    );

    private final JsonResourceLoader json;
    private Map<String, Object> bank;
    private Map<String, Object> scores;

    public MicrotaskBankService(JsonResourceLoader json) {
        this.json = json;
    }

    public String pickRandomSetId() {
        return SET_IDS.get(ThreadLocalRandom.current().nextInt(SET_IDS.size()));
    }

    public List<Map<String, Object>> buildMixedQuestionPlan(String jobId) {
        ensureJob(jobId);
        List<Map<String, Object>> plan = new ArrayList<>();
        for (int questionIndex = 0; questionIndex < QUESTIONS_PER_SET; questionIndex++) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("setId", pickRandomSetId());
            entry.put("questionIndex", questionIndex);
            plan.add(entry);
        }
        shuffle(plan);
        return plan;
    }

    public Map<String, Object> buildTemplate(String jobId, String setId) {
        if ("MIXED".equals(setId)) {
            return buildTemplateFromPlan(jobId, buildMixedQuestionPlan(jobId));
        }
        return buildSingleSetTemplate(jobId, setId);
    }

    public Map<String, Object> buildTemplateFromPlan(String jobId, List<Map<String, Object>> plan) {
        ensureJob(jobId);
        List<Map<String, Object>> steps = new ArrayList<>();
        for (int i = 0; i < plan.size(); i++) {
            Map<String, Object> entry = plan.get(i);
            String setId = String.valueOf(entry.get("setId"));
            int questionIndex = ((Number) entry.get("questionIndex")).intValue();
            Map<String, Object> question = questionAt(jobId, setId, questionIndex);
            steps.add(toStep(question, i + 1));
        }

        Map<String, Object> template = new HashMap<>();
        template.put("jobId", jobId);
        template.put("scaffoldType", "career_changer");
        template.put("title", themeForPlan(jobId, plan));
        template.put("scenario", themeForPlan(jobId, plan));
        template.put("setId", "MIXED");
        template.put("estimatedMinutes", 7);
        template.put("steps", steps);
        return template;
    }

    public Map<String, Object> buildContentForSession(TaskSession ts) {
        Map<String, Object> meta = ts.getTaskMeta();
        String setId = metaValue(meta, "setId");
        if ("MIXED".equals(setId)) {
            List<Map<String, Object>> plan = planFromMeta(meta);
            if (plan.isEmpty()) {
                throw new IllegalStateException("混合微任务缺少 questionPlan");
            }
            return buildTemplateFromPlan(ts.getJobId(), plan);
        }
        if (!setId.isBlank()) {
            return buildTemplate(ts.getJobId(), setId);
        }
        return buildSingleSetTemplate(ts.getJobId(), "A");
    }

    public Map<String, Object> buildRadarFromSteps(
            List<Map<String, Object>> stepsData,
            List<Map<String, Object>> stepDefs
    ) {
        List<Map<String, Object>> dimensions = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<Integer> scores = new ArrayList<>();

        for (int i = 0; i < stepsData.size() && i < stepDefs.size(); i++) {
            Map<String, Object> row = stepsData.get(i);
            Map<String, Object> def = stepDefs.get(i);
            String dimension = String.valueOf(def.getOrDefault("dimension", "维度" + (i + 1)));
            int raw = row.get("rawScore") != null
                    ? ((Number) row.get("rawScore")).intValue()
                    : resolveRawScore(def, extractOptionId(row.get("answer")));
            int radar = row.get("radarScore") != null
                    ? ((Number) row.get("radarScore")).intValue()
                    : toRadarScore(raw);

            Map<String, Object> dim = new HashMap<>();
            dim.put("name", dimension);
            dim.put("score", radar);
            dim.put("rawScore", raw);
            dimensions.add(dim);
            labels.add(dimension);
            scores.add(radar);
        }

        Map<String, Object> radar = new HashMap<>();
        radar.put("dimensions", dimensions);
        radar.put("labels", labels);
        radar.put("scores", scores);
        return radar;
    }

    public int resolveRawScore(Map<String, Object> stepDef, String optionId) {
        if (optionId == null || optionId.isBlank()) {
            return 0;
        }
        Object optionsObj = stepDef.get("options");
        if (optionsObj instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof Map<?, ?> option && String.valueOf(option.get("id")).equals(optionId)) {
                    Object score = option.get("score");
                    return score instanceof Number ? ((Number) score).intValue() : 0;
                }
            }
        }
        return 0;
    }

    public int toRadarScore(int raw) {
        return RADAR_SCORE_MAP.getOrDefault(raw, 0);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> buildSingleSetTemplate(String jobId, String setId) {
        ensureJob(jobId);
        Map<String, Object> job = jobOf(jobId);
        Map<String, Object> sets = (Map<String, Object>) job.get("sets");
        Map<String, Object> setData = (Map<String, Object>) sets.get(setId);
        if (setData == null) {
            throw new IllegalStateException("未找到微任务套题: " + jobId + "/" + setId);
        }

        List<Map<String, Object>> questions = (List<Map<String, Object>>) setData.get("questions");
        List<Map<String, Object>> steps = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            steps.add(toStep(enrichQuestion(jobId, setId, questions.get(i), i), i + 1));
        }

        Map<String, Object> template = new HashMap<>();
        template.put("jobId", jobId);
        template.put("scaffoldType", "career_changer");
        template.put("title", setData.get("theme"));
        template.put("scenario", setData.get("theme"));
        template.put("setId", setId);
        template.put("estimatedMinutes", 7);
        template.put("steps", steps);
        return template;
    }

    @SuppressWarnings("unchecked")
    private void ensureLoaded() {
        if (bank == null) {
            bank = json.load("seed/microtask-bank.json", new TypeReference<>() {});
        }
        if (scores == null) {
            scores = json.load("seed/microtask-scores.json", new TypeReference<>() {});
        }
    }

    @SuppressWarnings("unchecked")
    private void ensureJob(String jobId) {
        ensureLoaded();
        Map<String, Object> jobs = (Map<String, Object>) bank.get("jobs");
        if (jobs == null || !jobs.containsKey(jobId)) {
            throw new IllegalStateException("未找到岗位微任务题库: " + jobId);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> jobOf(String jobId) {
        ensureJob(jobId);
        Map<String, Object> jobs = (Map<String, Object>) bank.get("jobs");
        return (Map<String, Object>) jobs.get(jobId);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> questionAt(String jobId, String setId, int questionIndex) {
        Map<String, Object> job = jobOf(jobId);
        Map<String, Object> sets = (Map<String, Object>) job.get("sets");
        Map<String, Object> setData = (Map<String, Object>) sets.get(setId);
        List<Map<String, Object>> questions = (List<Map<String, Object>>) setData.get("questions");
        return enrichQuestion(jobId, setId, questions.get(questionIndex), questionIndex);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> enrichQuestion(
            String jobId,
            String setId,
            Map<String, Object> question,
            int questionIndex
    ) {
        Map<String, Object> enriched = new HashMap<>(question);
        Map<String, Object> jobs = (Map<String, Object>) scores.get("jobs");
        Map<String, Object> jobScores = (Map<String, Object>) jobs.get(jobId);
        if (jobScores != null) {
            Map<String, Object> sets = (Map<String, Object>) jobScores.get("sets");
            List<Map<String, Object>> scoreSet = (List<Map<String, Object>>) sets.get(setId);
            if (scoreSet != null && questionIndex < scoreSet.size()) {
                Map<String, Object> scoreDef = scoreSet.get(questionIndex);
                enriched.put("dimension", scoreDef.get("dimension"));
                enriched.put("options", copyOptionsWithScores(
                        (List<Map<String, Object>>) question.get("options"),
                        (List<Map<String, Object>>) scoreDef.get("options")
                ));
            }
        }
        return enriched;
    }

    private List<Map<String, Object>> copyOptionsWithScores(
            List<Map<String, Object>> publicOptions,
            List<Map<String, Object>> scoreOptions
    ) {
        Map<String, Integer> scoreById = new HashMap<>();
        for (Map<String, Object> score : scoreOptions) {
            scoreById.put(String.valueOf(score.get("id")), ((Number) score.get("score")).intValue());
        }
        List<Map<String, Object>> merged = new ArrayList<>();
        for (Map<String, Object> pub : publicOptions) {
            String id = String.valueOf(pub.get("id"));
            Map<String, Object> option = new HashMap<>();
            option.put("id", id);
            option.put("label", stripOptionPrefix(String.valueOf(pub.get("label"))));
            option.put("score", scoreById.getOrDefault(id, 0));
            merged.add(option);
        }
        return merged;
    }

    private String stripOptionPrefix(String label) {
        if (label.length() >= 2 && label.charAt(1) == '.' && Character.isUpperCase(label.charAt(0))) {
            return label.substring(2).trim();
        }
        return label;
    }

  @SuppressWarnings("unchecked")
    private Map<String, Object> toStep(Map<String, Object> question, int stepNumber) {
        List<Map<String, Object>> options = (List<Map<String, Object>>) question.get("options");
        Map<String, Object> step = new HashMap<>();
        step.put("stepNumber", stepNumber);
        step.put("stepTitle", "微任务 " + stepNumber);
        step.put("stepType", "microtask_choice");
        step.put("time", question.get("time"));
        step.put("speaker", question.get("speaker"));
        step.put("speakerRole", question.get("speakerRole"));
        step.put("message", question.get("message"));
        step.put("prompt", question.get("prompt"));
        step.put("dimension", question.get("dimension"));
        step.put("options", options);
        step.put("decision", Map.of(
                "type", "single_choice",
                "prompt", question.get("prompt"),
                "options", options
        ));
        return step;
    }

    @SuppressWarnings("unchecked")
    private String themeForPlan(String jobId, List<Map<String, Object>> plan) {
        if (plan.isEmpty()) {
            return "岗位微任务体验";
        }
        Map<String, Object> first = plan.get(0);
        String setId = String.valueOf(first.get("setId"));
        Map<String, Object> job = jobOf(jobId);
        Map<String, Object> sets = (Map<String, Object>) job.get("sets");
        Map<String, Object> setData = (Map<String, Object>) sets.get(setId);
        return String.valueOf(setData.get("theme"));
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> planFromMeta(Map<String, Object> meta) {
        Object planObj = meta.get("questionPlan");
        if (planObj instanceof List<?> list) {
            List<Map<String, Object>> plan = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Map<?, ?> raw) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("setId", String.valueOf(raw.get("setId")));
                    entry.put("questionIndex", ((Number) raw.get("questionIndex")).intValue());
                    plan.add(entry);
                }
            }
            return plan;
        }
        return List.of();
    }

    private String metaValue(Map<String, Object> meta, String key) {
        if (meta == null || meta.get(key) == null) {
            return "";
        }
        return String.valueOf(meta.get(key));
    }

    private String extractOptionId(Object answerObj) {
        if (answerObj instanceof Map<?, ?> answer) {
            Object id = answer.get("selectedOptionId");
            return id == null ? "" : String.valueOf(id);
        }
        return "";
    }

    private void shuffle(List<Map<String, Object>> plan) {
        for (int i = plan.size() - 1; i > 0; i--) {
            int j = ThreadLocalRandom.current().nextInt(i + 1);
            Map<String, Object> tmp = plan.get(i);
            plan.set(i, plan.get(j));
            plan.set(j, tmp);
        }
    }
}
