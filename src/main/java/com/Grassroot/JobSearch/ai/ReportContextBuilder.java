package com.Grassroot.JobSearch.ai;

import com.Grassroot.JobSearch.job.JobModel;
import com.Grassroot.JobSearch.job.JobRepository;
import com.Grassroot.JobSearch.scene.SceneCatalogService;
import com.Grassroot.JobSearch.scene.SceneEvidence;
import com.Grassroot.JobSearch.scene.SceneEvidenceRepository;
import com.Grassroot.JobSearch.session.UserSession;
import com.Grassroot.JobSearch.task.InterestSignal;
import com.Grassroot.JobSearch.task.InterestSignalRepository;
import com.Grassroot.JobSearch.task.MicrotaskBankService;
import com.Grassroot.JobSearch.task.TaskSession;
import com.Grassroot.JobSearch.task.TaskSessionRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ReportContextBuilder {

    private final SceneEvidenceRepository sceneEvidenceRepository;
    private final SceneCatalogService sceneCatalogService;
    private final TaskSessionRepository taskSessionRepository;
    private final InterestSignalRepository interestSignalRepository;
    private final JobRepository jobRepository;
    private final MicrotaskBankService microtaskBank;

    public ReportContextBuilder(
            SceneEvidenceRepository sceneEvidenceRepository,
            SceneCatalogService sceneCatalogService,
            TaskSessionRepository taskSessionRepository,
            InterestSignalRepository interestSignalRepository,
            JobRepository jobRepository,
            MicrotaskBankService microtaskBank) {
        this.sceneEvidenceRepository = sceneEvidenceRepository;
        this.sceneCatalogService = sceneCatalogService;
        this.taskSessionRepository = taskSessionRepository;
        this.interestSignalRepository = interestSignalRepository;
        this.jobRepository = jobRepository;
        this.microtaskBank = microtaskBank;
    }

    public Map<String, Object> build(String sessionId, UserSession session) {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("resume_evidences", session.getResumeEvidenceData() == null ? Map.of() : session.getResumeEvidenceData());
        ctx.put("scene_evidences", buildSceneEvidenceList(sessionId));
        ctx.put("task_evidences", buildTaskEvidenceByJob(sessionId));
        ctx.put("interest_signals", buildInterestSignals(sessionId));
        String targetJob = resolveTargetJob(sessionId);
        ctx.put("selected_target_job", targetJob);
        ctx.put("microtask_choice_signals", buildMicrotaskChoiceSignals(sessionId, targetJob));
        ctx.put("microtask_capability_summary", buildMicrotaskCapabilitySummary(sessionId, targetJob));
        ctx.put("user_subjective_highlights", buildSubjectiveHighlights(sessionId, targetJob));
        return ctx;
    }

    /**
     * 供行为信号合成：情景自定义回答的结构化信号。
     */
    public List<Map<String, Object>> buildSceneBehaviorSignals(String sessionId) {
        List<Map<String, Object>> signals = new ArrayList<>();
        int sceneIndex = 0;
        for (SceneEvidence ev : sceneEvidenceRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)) {
            if (!"custom".equalsIgnoreCase(ev.getAnswerType())) {
                continue;
            }
            String words = ev.getRawAnswer() == null ? "" : ev.getRawAnswer().trim();
            if (words.isBlank()) {
                continue;
            }
            Map<String, Object> sceneScript = safeSceneScript(ev.getSceneId());
            signals.add(buildSceneSignal(ev, sceneScript, sceneIndex++));
        }
        return signals;
    }

  @SuppressWarnings("unchecked")
    public List<Map<String, Object>> buildMicrotaskCapabilitySummary(String sessionId, String backendJobId) {
        if (backendJobId == null || backendJobId.isBlank()) {
            return List.of();
        }
        Map<String, Object> radar = buildTaskRadar(sessionId, backendJobId);
        List<String> dimensions = castStringList(radar.get("dimensions"));
        List<Integer> scores = castIntList(radar.get("scores"));
        List<Map<String, Object>> summary = new ArrayList<>();
        for (int i = 0; i < dimensions.size(); i++) {
            int score = i < scores.size() ? scores.get(i) : 0;
            String dim = dimensions.get(i);
            Map<String, Object> row = new HashMap<>();
            row.put("dimension", dim);
            row.put("tendency", capabilityTendency(score));
            row.put("evidenceHint", capabilityHint(dim, score));
            summary.add(row);
        }
        return summary;
    }

    private Map<String, Object> buildSceneSignal(SceneEvidence ev, Map<String, Object> sceneScript, int index) {
        String context = stringVal(sceneScript.get("context"));
        String question = stringVal(sceneScript.get("question"));
        String title = stringVal(sceneScript.get("title"));
        String userWords = ev.getRawAnswer() == null ? "" : ev.getRawAnswer().trim();
        Map<String, Object> cap = extractCapabilityAnalysis(ev);

        String sceneClip = clip(context.isBlank() ? title : context, 36);
        String observation = "情境：" + sceneClip + "。你的回应：" + clip(userWords, 40) + "。";
        String insight = buildSceneInsight(cap, userWords, ev.getObservedBehavior());
        String tendency = stringVal(cap.get("tendency"));
        if (tendency.isBlank()) {
            tendency = inferSceneTendency(cap);
        }

        Map<String, Object> signal = new HashMap<>();
        signal.put("step", 200 + index);
        signal.put("dimension", "情景：" + (title.isBlank() ? "自定义回应" : title));
        signal.put("source", "scene");
        signal.put("subjective", true);
        signal.put("emotional", isEmotionalLabel(userWords));
        signal.put("optionId", "CUSTOM");
        signal.put("abilityTendency", tendency.isBlank() ? "mixed" : tendency);
        signal.put("lead", "你先按自己的方式回应了这个情境。");
        signal.put("observation", observation);
        signal.put("insight", insight);
        if ("gap".equals(tendency) || "mixed".equals(tendency) || "stress".equals(tendency)) {
            signal.put("gapNote", clip(stringVal(cap.get("scene_link")), 48));
        }
        signal.put("score", 60);
        signal.put("rawScore", 3);
        return signal;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractCapabilityAnalysis(SceneEvidence ev) {
        Map<String, Object> cap = new HashMap<>();
        if (ev.getWorkstyleEvidence() != null && ev.getWorkstyleEvidence().get("capability_analysis") instanceof Map<?, ?> m) {
            cap.putAll((Map<String, Object>) m);
        }
        if (cap.isEmpty() && ev.getEvidenceSummary() != null && !ev.getEvidenceSummary().isBlank()) {
            cap.put("scene_link", ev.getEvidenceSummary());
        }
        return cap;
    }

    private String buildSceneInsight(Map<String, Object> cap, String userWords, String observed) {
        String link = stringVal(cap.get("scene_link"));
        if (!link.isBlank()) {
            return clip(link, 80);
        }
        @SuppressWarnings("unchecked")
        List<String> strengths = cap.get("strengths") instanceof List<?> list
                ? (List<String>) list
                : List.of();
        @SuppressWarnings("unchecked")
        List<String> gaps = cap.get("gaps") instanceof List<?> list
                ? (List<String>) list
                : List.of();
        if (!strengths.isEmpty() && !gaps.isEmpty()) {
            return clip("擅长：" + strengths.get(0) + "；还可加强：" + gaps.get(0), 80);
        }
        if (!strengths.isEmpty()) {
            return clip("擅长：" + strengths.get(0), 80);
        }
        if (!gaps.isEmpty()) {
            return clip("还可加强：" + gaps.get(0), 80);
        }
        if (observed != null && !observed.isBlank()) {
            return clip(observed, 80);
        }
        return "回应与情境直接相关，可继续观察取舍与推进方式。";
    }

    private String inferSceneTendency(Map<String, Object> cap) {
        @SuppressWarnings("unchecked")
        List<String> strengths = cap.get("strengths") instanceof List<?> list
                ? (List<String>) list
                : List.of();
        @SuppressWarnings("unchecked")
        List<String> gaps = cap.get("gaps") instanceof List<?> list
                ? (List<String>) list
                : List.of();
        if (!strengths.isEmpty() && gaps.isEmpty()) {
            return "strength";
        }
        if (strengths.isEmpty() && !gaps.isEmpty()) {
            return "gap";
        }
        return "mixed";
    }

    private Map<String, Object> safeSceneScript(String sceneId) {
        try {
            return sceneCatalogService.getScene(sceneId);
        } catch (Exception ex) {
            return Map.of();
        }
    }

    private String capabilityTendency(int radarScore) {
        if (radarScore >= 80) {
            return "strength";
        }
        if (radarScore >= 60) {
            return "mixed";
        }
        return "gap";
    }

    private String capabilityHint(String dimension, int radarScore) {
        if (radarScore >= 80) {
            return "【" + dimension + "】本轮判断较顺，是相对擅长的方向。";
        }
        if (radarScore >= 60) {
            return "【" + dimension + "】本轮有显现，还可进一步深化。";
        }
        return "【" + dimension + "】本轮信号偏弱，值得关注加强。";
    }

    private String clip(String text, int max) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String t = text.trim();
        return t.length() <= max ? t : t.substring(0, max - 1) + "…";
    }

    @SuppressWarnings("unchecked")
    private List<String> castStringList(Object v) {
        if (v instanceof List<?> list) {
            List<String> out = new ArrayList<>();
            for (Object item : list) {
                if (item != null) {
                    out.add(String.valueOf(item));
                }
            }
            return out;
        }
        return List.of();
    }

    private List<Integer> castIntList(Object v) {
        if (v instanceof List<?> list) {
            List<Integer> out = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof Number n) {
                    out.add(n.intValue());
                }
            }
            return out;
        }
        return List.of();
    }

    /**
     * 供报告 API 返回雷达图；不向大模型传递具体分数。
     */
    public Map<String, Object> buildTaskRadar(String sessionId, String backendJobId) {
        if (backendJobId == null || backendJobId.isBlank()) {
            return Map.of("dimensions", List.of(), "labels", List.of(), "scores", List.of());
        }
        return taskSessionRepository.findBySessionIdOrderByStartedAtDesc(sessionId).stream()
                .filter(ts -> backendJobId.equals(ts.getJobId()))
                .filter(ts -> ts.getStatus() == com.Grassroot.JobSearch.common.enums.TaskSessionStatus.completed)
                .findFirst()
                .map(ts -> radarForTask(ts))
                .orElse(Map.of("dimensions", List.of(), "labels", List.of(), "scores", List.of()));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> radarForTask(TaskSession ts) {
        Map<String, Object> meta = ts.getTaskMeta();
        String setId = meta != null && meta.get("setId") != null ? String.valueOf(meta.get("setId")) : "A";
        Map<String, Object> content = microtaskBank.buildContentForSession(ts);
        List<Map<String, Object>> stepDefs = (List<Map<String, Object>>) content.get("steps");
        Map<String, Object> radar = microtaskBank.buildRadarFromSteps(ts.getStepsData(), stepDefs);
        radar.put("jobId", ts.getJobId());
        radar.put("setId", setId);
        return radar;
    }

    private static String stringVal(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> buildMicrotaskChoiceSignals(String sessionId, String backendJobId) {
        if (backendJobId == null || backendJobId.isBlank()) {
            return List.of();
        }
        return taskSessionRepository.findBySessionIdOrderByStartedAtDesc(sessionId).stream()
                .filter(ts -> backendJobId.equals(ts.getJobId()))
                .filter(ts -> ts.getStatus() == com.Grassroot.JobSearch.common.enums.TaskSessionStatus.completed)
                .findFirst()
                .map(ts -> choiceSignalsForTask(ts))
                .orElse(List.of());
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> choiceSignalsForTask(TaskSession ts) {
        Map<String, Object> content = microtaskBank.buildContentForSession(ts);
        List<Map<String, Object>> stepDefs = (List<Map<String, Object>>) content.get("steps");
        List<Map<String, Object>> signals = new ArrayList<>();
        List<Map<String, Object>> stepsData = ts.getStepsData();
        for (int i = 0; i < stepsData.size() && i < stepDefs.size(); i++) {
            Map<String, Object> row = stepsData.get(i);
            Map<String, Object> def = stepDefs.get(i);
            String optionId = extractSelectedOptionId(row.get("answer"));
            String label = resolveOptionLabel(def, optionId);
            Map<String, Object> signal = new HashMap<>();
            signal.put("step", row.get("step"));
            signal.put("dimension", def.get("dimension"));
            signal.put("time", def.get("time"));
            signal.put("speaker", def.get("speaker"));
            signal.put("speakerRole", def.get("speakerRole"));
            signal.put("scenario", def.get("message"));
            signal.put("prompt", def.get("prompt"));
            signal.put("selectedOption", label);
            signal.put("selectedOptionId", optionId);
            signal.put("otherOptions", otherOptionLabels(def, optionId));
            boolean subjective = isSubjectiveOption(optionId, label);
            signal.put("answerNature", subjective ? "subjective" : "capability");
            signal.put("priority", subjective ? "high" : "normal");
            signals.add(signal);
        }
        return signals;
    }

    @SuppressWarnings("unchecked")
    private List<String> otherOptionLabels(Map<String, Object> stepDef, String selectedId) {
        Object optionsObj = stepDef.get("options");
        if (optionsObj == null && stepDef.get("decision") instanceof Map<?, ?> decision) {
            optionsObj = decision.get("options");
        }
        List<String> labels = new ArrayList<>();
        if (optionsObj instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof Map<?, ?> option) {
                    String id = String.valueOf(option.get("id"));
                    if (!id.equals(selectedId)) {
                        labels.add(String.valueOf(option.get("label")));
                    }
                }
            }
        }
        return labels;
    }

    @SuppressWarnings("unchecked")
    private String resolveOptionLabel(Map<String, Object> stepDef, String optionId) {
        if (optionId == null || optionId.isBlank()) {
            return "";
        }
        Object decision = stepDef.get("decision");
        if (decision instanceof Map<?, ?> decisionMap) {
            return labelFromOptions(decisionMap.get("options"), optionId);
        }
        return labelFromOptions(stepDef.get("options"), optionId);
    }

    @SuppressWarnings("unchecked")
    private String labelFromOptions(Object optionsObj, String optionId) {
        if (optionsObj instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof Map<?, ?> option && String.valueOf(option.get("id")).equals(optionId)) {
                    return String.valueOf(option.get("label"));
                }
            }
        }
        return optionId;
    }

    @SuppressWarnings("unchecked")
    private String extractSelectedOptionId(Object answerObj) {
        if (answerObj instanceof Map<?, ?> answer) {
            Object id = answer.get("selectedOptionId");
            return id == null ? "" : String.valueOf(id);
        }
        return "";
    }

    public Map<String, Object> buildTaskEvidenceByJob(String sessionId) {
        Map<String, Object> byJob = new LinkedHashMap<>();
        for (SceneEvidence ev : sceneEvidenceRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)) {
            byJob.put(ev.getRoleId(), toTaskEvidenceEntry(ev));
        }
        for (TaskSession ts : taskSessionRepository.findBySessionIdOrderByStartedAtDesc(sessionId)) {
            if (!byJob.containsKey(ts.getJobId())) {
                byJob.put(ts.getJobId(), minimalTaskEvidence(ts));
            }
        }
        return byJob;
    }

    private List<Map<String, Object>> buildSceneEvidenceList(String sessionId) {
        return sceneEvidenceRepository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(this::toSceneMap)
                .toList();
    }

    private List<Map<String, Object>> buildInterestSignals(String sessionId) {
        List<TaskSession> tasks = taskSessionRepository.findBySessionIdOrderByStartedAtDesc(sessionId);
        if (tasks.isEmpty()) {
            return List.of();
        }
        List<String> taskIds = tasks.stream().map(TaskSession::getId).toList();
        Map<String, String> jobByTask = tasks.stream()
                .collect(Collectors.toMap(TaskSession::getId, TaskSession::getJobId, (a, b) -> a));
        return interestSignalRepository.findByTaskSessionIdIn(taskIds).stream()
                .map(sig -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("jobId", jobByTask.get(sig.getTaskSessionId()));
                    m.put("likeLevel", sig.getLikeLevel());
                    m.put("longTermWillingness", sig.getLongTermWillingness());
                    m.put("feelingSource", sig.getFeelingSource());
                    m.put("freeText", sig.getFreeText());
                    return m;
                })
                .toList();
    }

    private String resolveTargetJob(String sessionId) {
        List<SceneEvidence> scenes = sceneEvidenceRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        if (!scenes.isEmpty()) {
            return scenes.get(scenes.size() - 1).getRoleId();
        }
        return taskSessionRepository.findBySessionIdOrderByStartedAtDesc(sessionId).stream()
                .findFirst()
                .map(TaskSession::getJobId)
                .orElse(null);
    }

    private Map<String, Object> toSceneMap(SceneEvidence ev) {
        Map<String, Object> m = new HashMap<>();
        m.put("sceneId", ev.getSceneId());
        m.put("roleId", ev.getRoleId());
        m.put("answerType", ev.getAnswerType());
        m.put("observedBehavior", ev.getObservedBehavior());
        m.put("workstyleEvidence", ev.getWorkstyleEvidence());
        m.put("roleTags", ev.getRoleTags());
        m.put("evidenceSummary", ev.getEvidenceSummary());
        m.put("confidence", ev.getConfidence());
        m.put("rawAnswer", ev.getRawAnswer());
        Map<String, Object> cap = extractCapabilityAnalysis(ev);
        if (!cap.isEmpty()) {
            m.put("capability_analysis", cap);
        }
        return m;
    }

    private List<Map<String, Object>> buildSubjectiveHighlights(String sessionId, String backendJobId) {
        List<Map<String, Object>> highlights = new ArrayList<>();
        for (Map<String, Object> signal : buildMicrotaskChoiceSignals(sessionId, backendJobId)) {
            String optionId = stringVal(signal.get("selectedOptionId"));
            if (!"C".equalsIgnoreCase(optionId)) {
                continue;
            }
            Map<String, Object> row = new HashMap<>();
            row.put("source", "microtask");
            row.put("step", signal.get("step"));
            row.put("dimension", signal.get("dimension"));
            row.put("userWords", stripOptionPrefix(stringVal(signal.get("selectedOption"))));
            row.put("scenario", signal.get("scenario"));
            row.put("prompt", signal.get("prompt"));
            row.put("answerNature", "subjective");
            row.put("priority", "high");
            highlights.add(row);
        }
        for (SceneEvidence ev : sceneEvidenceRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)) {
            if (!"custom".equalsIgnoreCase(ev.getAnswerType())) {
                continue;
            }
            String words = ev.getRawAnswer() == null ? "" : ev.getRawAnswer().trim();
            if (words.isBlank()) {
                continue;
            }
            Map<String, Object> row = new HashMap<>();
            row.put("source", "scene");
            row.put("sceneId", ev.getSceneId());
            row.put("userWords", words);
            row.put("evidenceSummary", ev.getEvidenceSummary());
            Map<String, Object> sceneScript = safeSceneScript(ev.getSceneId());
            row.put("sceneContext", clip(stringVal(sceneScript.get("context")), 120));
            row.put("sceneQuestion", stringVal(sceneScript.get("question")));
            row.put("answerNature", "subjective");
            row.put("priority", "high");
            highlights.add(row);
        }
        return highlights;
    }

    private boolean isSubjectiveOption(String optionId, String label) {
        if ("C".equalsIgnoreCase(optionId)) {
            return true;
        }
        String stripped = stripOptionPrefix(label);
        return isEmotionalLabel(stripped) || isEmotionalLabel(label);
    }

    private boolean isEmotionalLabel(String label) {
        if (label == null || label.isBlank()) {
            return false;
        }
        return label.contains("辞职")
                || label.contains("想辞职")
                || label.contains("不想")
                || label.contains("算了")
                || label.contains("放弃")
                || label.contains("崩溃")
                || label.contains("受不了")
                || label.contains("烦")
                || label.contains("摆烂");
    }

    private String stripOptionPrefix(String label) {
        if (label == null || label.length() < 2) {
            return label == null ? "" : label.trim();
        }
        if (label.charAt(1) == '.' && Character.isUpperCase(label.charAt(0))) {
            return label.substring(2).trim();
        }
        return label.trim();
    }

    private Map<String, Object> toTaskEvidenceEntry(SceneEvidence ev) {
        String jobName = jobRepository.findById(ev.getRoleId()).map(JobModel::getName).orElse(ev.getRoleId());
        List<Map<String, Object>> behaviors = new ArrayList<>();
        if (ev.getObservedBehavior() != null && !ev.getObservedBehavior().isBlank()) {
            Map<String, Object> row = new HashMap<>();
            row.put("competency", primaryCompetency(ev.getWorkstyleEvidence()));
            row.put("behavior", ev.getObservedBehavior());
            row.put("judgment", "本轮观察");
            row.put("confidence", ev.getConfidence() >= 0.8 ? "中" : "初步");
            row.put("note", ev.getEvidenceSummary());
            behaviors.add(row);
        }
        Map<String, Object> entry = new HashMap<>();
        entry.put("jobName", jobName);
        entry.put("headline", ev.getEvidenceSummary());
        entry.put("comparisonSummary", ev.getObservedBehavior());
        entry.put("observedBehaviors", behaviors);
        entry.put("roleTags", ev.getRoleTags());
        entry.put("workstyleEvidence", ev.getWorkstyleEvidence());
        entry.put("source", "meeting_scene");
        return entry;
    }

    private Map<String, Object> minimalTaskEvidence(TaskSession ts) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("jobName", jobRepository.findById(ts.getJobId()).map(JobModel::getName).orElse(ts.getJobId()));
        entry.put("headline", "已完成微任务步骤 " + ts.getCurrentStep() + " / 6");
        entry.put("comparisonSummary", "本轮微任务答案已记录，详细行为分析待补充。");
        entry.put("observedBehaviors", List.of());
        entry.put("stepsData", ts.getStepsData());
        entry.put("source", "micro_task");
        return entry;
    }

    @SuppressWarnings("unchecked")
    private String primaryCompetency(Map<String, Object> workstyle) {
        if (workstyle == null || workstyle.isEmpty()) {
            return "工作方式";
        }
        return switch (workstyle.keySet().iterator().next()) {
            case "decision_style" -> "决策取舍";
            case "conflict_style" -> "分歧处理";
            case "communication_style" -> "沟通表达";
            default -> "工作方式";
        };
    }
}
