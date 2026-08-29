package com.Grassroot.JobSearch.ai;

import com.Grassroot.JobSearch.task.MicrotaskBankService;
import com.Grassroot.JobSearch.task.TaskSession;
import com.Grassroot.JobSearch.task.TaskSessionRepository;
import com.Grassroot.JobSearch.common.enums.TaskSessionStatus;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class JudgmentBasisComposer {

    private static final Pattern EMOTIONAL_LABEL =
            Pattern.compile("辞职|想辞职|不想干|不想做|不想继续|算了|放弃|太累|崩溃|受不了|没意义|懒得|沮丧|干不下去|想逃|烦死|好烦|摆烂|倦怠|熬不下去");

    private final ReportContextBuilder reportContextBuilder;
    private final TaskSessionRepository taskSessionRepository;
    private final MicrotaskBankService microtaskBank;

    public JudgmentBasisComposer(
            ReportContextBuilder reportContextBuilder,
            TaskSessionRepository taskSessionRepository,
            MicrotaskBankService microtaskBank) {
        this.reportContextBuilder = reportContextBuilder;
        this.taskSessionRepository = taskSessionRepository;
        this.microtaskBank = microtaskBank;
    }

    public List<String> compose(String sessionId, String backendJobId) {
        Map<String, Object> pack = composeSignals(sessionId, backendJobId, Map.of());
        List<Map<String, Object>> all = castSignalList(pack.get("allEvidence"));
        List<String> lines = new ArrayList<>();
        for (Map<String, Object> signal : all) {
            lines.add(flattenSignal(signal));
        }
        return lines;
    }

    public Map<String, Object> composeSignals(
            String sessionId,
            String backendJobId,
            Map<String, Object> taskRadar) {
        List<Map<String, Object>> choiceSignals =
                reportContextBuilder.buildMicrotaskChoiceSignals(sessionId, backendJobId);
        if (choiceSignals.isEmpty()) {
            return Map.of("topSignals", List.of(), "allEvidence", List.of());
        }

        List<Integer> radarScores = castIntList(taskRadar.get("scores"));
        List<Map<String, Object>> allEvidence = new ArrayList<>();
        for (int i = 0; i < choiceSignals.size(); i++) {
            Map<String, Object> row = choiceSignals.get(i);
            int radarScore = i < radarScores.size() ? radarScores.get(i) : 0;
            int rawScore = resolveRawScore(sessionId, backendJobId, row, i);
            if (radarScore == 0 && rawScore > 0) {
                radarScore = microtaskBank.toRadarScore(rawScore);
            }
            allEvidence.add(buildSignal(row, rawScore, radarScore));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("allEvidence", allEvidence);
        result.put("topSignals", pickTopSignals(allEvidence));
        return result;
    }

    private int resolveRawScore(String sessionId, String backendJobId, Map<String, Object> row, int index) {
        String selectedLabel = stripOptionPrefix(stringVal(row.get("selectedOption")));
        if (isEmotionalOrAvoidantLabel(selectedLabel)) {
            return 2;
        }
        TaskSession ts = taskSessionRepository.findBySessionIdOrderByStartedAtDesc(sessionId).stream()
                .filter(s -> backendJobId.equals(s.getJobId()))
                .filter(s -> s.getStatus() == TaskSessionStatus.completed)
                .findFirst()
                .orElse(null);
        if (ts == null) {
            return inferRawFromSelected(row);
        }
        List<Map<String, Object>> stepsData = ts.getStepsData();
        if (stepsData != null && index < stepsData.size()) {
            Map<String, Object> stepRow = stepsData.get(index);
            Object raw = stepRow.get("rawScore");
            if (raw instanceof Number n) {
                return n.intValue();
            }
        }
        Map<String, Object> content = microtaskBank.buildContentForSession(ts);
        List<Map<String, Object>> steps = castMapList(content.get("steps"));
        int step = numberVal(row.get("step"), 1);
        if (step <= 0 || step > steps.size()) {
            return inferRawFromSelected(row);
        }
        String optionId = stringVal(row.get("selectedOptionId"));
        if (optionId.isBlank()) {
            optionId = extractOptionIdFromLabel(stringVal(row.get("selectedOption")));
        }
        return microtaskBank.resolveRawScore(steps.get(step - 1), optionId);
    }

    private Map<String, Object> buildSignal(Map<String, Object> row, int rawScore, int radarScore) {
        String dimension = stringVal(row.get("dimension"));
        if (dimension.isBlank()) {
            dimension = "行为信号";
        }
        String prompt = stringVal(row.get("prompt"));
        String message = stringVal(row.get("scenario"));
        String selectedRawLabel = stringVal(row.get("selectedOption"));
        String selectedLabel = stripOptionPrefix(selectedRawLabel);
        String optionId = stringVal(row.get("selectedOptionId"));
        boolean emotional = isEmotionalOrAvoidantLabel(selectedLabel) || isEmotionalOrAvoidantLabel(selectedRawLabel);
        boolean subjective = "C".equalsIgnoreCase(optionId) || emotional;

        Map<String, Object> signal = new HashMap<>();
        signal.put("step", numberVal(row.get("step"), 0));
        signal.put("dimension", dimension);
        signal.put("optionId", optionId);
        signal.put("subjective", subjective);
        signal.put("emotional", emotional);
        if (subjective) {
            signal.put("lead", buildSubjectiveLead(emotional));
            signal.put("observation", buildSubjectiveObservation(message, selectedLabel, emotional));
            signal.put("insight", buildSubjectiveInsight(dimension, emotional));
            if (emotional) {
                signal.put("gapNote", buildSubjectiveGapNote(emotional));
            }
        } else {
            signal.put("lead", buildLead(prompt, false));
            signal.put("observation", buildObservation(message, selectedLabel, false));
            signal.put("insight", buildInsight(dimension, rawScore, false));
            if (radarScore < 100) {
                signal.put("gapNote", buildGapNote(rawScore, false));
            }
        }
        signal.put("score", radarScore);
        signal.put("rawScore", rawScore);
        return signal;
    }

    public Map<String, Object> mergeWithLlmSignals(Map<String, Object> composed, Map<String, Object> llmPack) {
        if (llmPack == null || llmPack.isEmpty()) {
            return composed;
        }
        List<Map<String, Object>> baseAll = castSignalList(composed.get("allEvidence"));
        List<Map<String, Object>> llmAll = castSignalList(llmPack.get("allEvidence"));
        if (baseAll.isEmpty()) {
            Map<String, Object> out = new HashMap<>();
            out.put("allEvidence", llmAll);
            out.put("topSignals", pickTopSignals(llmAll));
            return out;
        }
        Map<Integer, Map<String, Object>> llmByStep = new HashMap<>();
        for (Map<String, Object> signal : llmAll) {
            llmByStep.put(numberVal(signal.get("step"), 0), signal);
        }
        List<Map<String, Object>> merged = new ArrayList<>();
        for (Map<String, Object> signal : baseAll) {
            int step = numberVal(signal.get("step"), 0);
            Map<String, Object> llmMatch = llmByStep.get(step);
            if (llmMatch != null && shouldPreferLlm(signal, llmMatch)) {
                merged.add(overlayLlmSignal(signal, llmMatch));
            } else {
                merged.add(signal);
            }
        }
        for (Map<String, Object> signal : llmAll) {
            int step = numberVal(signal.get("step"), 0);
            boolean exists = false;
            for (Map<String, Object> existing : merged) {
                if (numberVal(existing.get("step"), 0) == step) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                merged.add(signal);
            }
        }
        Map<String, Object> out = new HashMap<>();
        out.put("allEvidence", merged);
        out.put("topSignals", pickTopSignals(merged));
        return out;
    }

    private boolean shouldPreferLlm(Map<String, Object> base, Map<String, Object> llm) {
        if (!Boolean.TRUE.equals(base.get("subjective"))) {
            return false;
        }
        String llmObs = stringVal(llm.get("observation"));
        return !llmObs.isBlank() && llmObs.length() >= stringVal(base.get("observation")).length();
    }

    private Map<String, Object> overlayLlmSignal(Map<String, Object> base, Map<String, Object> llm) {
        Map<String, Object> out = new HashMap<>(base);
        copyIfPresent(out, llm, "lead");
        copyIfPresent(out, llm, "observation");
        copyIfPresent(out, llm, "insight");
        copyIfPresent(out, llm, "gapNote");
        return out;
    }

    private void copyIfPresent(Map<String, Object> target, Map<String, Object> source, String key) {
        String value = stringVal(source.get(key));
        if (value.isBlank()) {
            return;
        }
        int max = "observation".equals(key) ? 80 : "insight".equals(key) ? 56 : 48;
        target.put(key, clip(value, max));
    }

    private List<Map<String, Object>> pickTopSignals(List<Map<String, Object>> allEvidence) {
        List<Map<String, Object>> sorted = new ArrayList<>(allEvidence);
        sorted.sort(Comparator
                .comparing((Map<String, Object> s) -> Boolean.TRUE.equals(s.get("subjective")))
                .reversed()
                .thenComparing((Map<String, Object> s) -> Boolean.TRUE.equals(s.get("emotional")))
                .reversed()
                .thenComparing(Comparator.comparingInt((Map<String, Object> s) -> numberVal(s.get("rawScore"), 0)).reversed())
                .thenComparing(Comparator.comparingInt((Map<String, Object> s) -> numberVal(s.get("score"), 0)).reversed()));

        List<Map<String, Object>> picked = new ArrayList<>();
        Set<String> seenDimensions = new HashSet<>();
        for (Map<String, Object> signal : sorted) {
            if (picked.size() >= 3) {
                break;
            }
            String dimension = stringVal(signal.get("dimension"));
            if (seenDimensions.contains(dimension)) {
                continue;
            }
            seenDimensions.add(dimension);
            picked.add(signal);
        }
        for (Map<String, Object> signal : sorted) {
            if (picked.size() >= 3) {
                break;
            }
            if (!picked.contains(signal)) {
                picked.add(signal);
            }
        }

        Map<String, Object> primarySubjective = null;
        for (Map<String, Object> signal : allEvidence) {
            if (Boolean.TRUE.equals(signal.get("subjective"))) {
                primarySubjective = signal;
                break;
            }
        }
        if (primarySubjective != null) {
            boolean alreadyPicked = false;
            int pickedIndex = -1;
            for (int i = 0; i < picked.size(); i++) {
                if (numberVal(picked.get(i).get("step"), 0) == numberVal(primarySubjective.get("step"), 0)) {
                    alreadyPicked = true;
                    pickedIndex = i;
                    break;
                }
            }
            if (!alreadyPicked) {
                if (picked.size() >= 3) {
                    picked.set(0, primarySubjective);
                } else {
                    picked.add(0, primarySubjective);
                }
            } else if (pickedIndex > 0) {
                Map<String, Object> moved = picked.remove(pickedIndex);
                picked.add(0, moved);
            }
        }

        return picked.size() > 3 ? picked.subList(0, 3) : picked;
    }

    private String buildSubjectiveLead(boolean emotional) {
        if (emotional) {
            return "你先回应了当下的压力。";
        }
        return "你先说了真实想法。";
    }

    private String buildSubjectiveObservation(String message, String selectedLabel, boolean emotional) {
        String focus = clip(selectedLabel.isBlank() ? "此刻的感受" : selectedLabel, 48);
        if (emotional) {
            return "你的第一反应：" + focus + "。";
        }
        return "你的主观回应：" + focus + "。";
    }

    private String buildSubjectiveInsight(String dimension, boolean emotional) {
        if (emotional) {
            return "压力先占满判断空间，需追问哪个环节不可持续。";
        }
        return "自身体验先于标准解题路径。";
    }

    private String buildSubjectiveGapNote(boolean emotional) {
        if (!emotional) {
            return "";
        }
        return "这不等于不适合岗位，先分清压力来自任务、协作还是节奏。";
    }

    private boolean isEmotionalOrAvoidantLabel(String label) {
        if (label == null || label.isBlank()) {
            return false;
        }
        return EMOTIONAL_LABEL.matcher(label).find();
    }

    private String buildLead(String prompt, boolean emotional) {
        if (emotional) {
            return "你先回应了当下的压力。";
        }
        String core = promptCore(prompt);
        if (core.isBlank()) {
            return "你做出了一个判断。";
        }
        return "你先想：" + clip(core, 40) + "。";
    }

    private String buildObservation(String message, String selectedLabel, boolean emotional) {
        String focus = clip(selectedLabel.isBlank() ? "关键线索" : selectedLabel, 48);
        if (emotional) {
            return "你的第一反应：" + focus + "。";
        }
        return "你优先关注：" + focus + "。";
    }

    private String buildInsight(String dimension, int rawScore, boolean emotional) {
        if (emotional) {
            return "压力先占满判断空间，需追问不可持续的环节。";
        }
        if (rawScore >= 5) {
            return "判断路径清晰，能往动机与价值推进。";
        }
        if (rawScore >= 4) {
            return "开始从现象追问原因。";
        }
        if (rawScore >= 3) {
            return "更关注局部信号，因果还可再展开。";
        }
        return "判断尚初步，分析层次还可加深。";
    }

    private String buildGapNote(int rawScore, boolean emotional) {
        if (emotional) {
            return buildSubjectiveGapNote(true);
        }
        if (rawScore >= 5) {
            return "";
        }
        if (rawScore >= 4) {
            return "可补一条外部数据，结论会更稳。";
        }
        return "";
    }

    private String flattenSignal(Map<String, Object> signal) {
        List<String> parts = new ArrayList<>();
        parts.add(stringVal(signal.get("lead")));
        parts.add(stringVal(signal.get("observation")));
        parts.add(stringVal(signal.get("insight")));
        String gap = stringVal(signal.get("gapNote"));
        if (!gap.isBlank()) {
            parts.add(gap);
        }
        return String.join(" ", parts).trim();
    }

    private int inferRawFromSelected(Map<String, Object> row) {
        String selected = stringVal(row.get("selectedOption"));
        if (isEmotionalOrAvoidantLabel(stripOptionPrefix(selected)) || isEmotionalOrAvoidantLabel(selected)) {
            return 2;
        }
        if (selected.startsWith("A.") || selected.startsWith("A ")) {
            return 4;
        }
        return 3;
    }

    private String extractOptionIdFromLabel(String label) {
        if (label.length() >= 2 && label.charAt(1) == '.' && Character.isUpperCase(label.charAt(0))) {
            return String.valueOf(label.charAt(0));
        }
        return "";
    }

    private String stripOptionPrefix(String label) {
        if (label.length() >= 2 && label.charAt(1) == '.' && Character.isUpperCase(label.charAt(0))) {
            return label.substring(2).trim();
        }
        return label.trim();
    }

    private String promptCore(String prompt) {
        String p = prompt == null ? "" : prompt.trim();
        return p.replaceAll("[？?]$", "");
    }

    private String clip(String text, int max) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String t = text.trim();
        return t.length() <= max ? t : t.substring(0, max - 1) + "…";
    }

    private String stringVal(Object v) {
        return v == null ? "" : String.valueOf(v).trim();
    }

    private int numberVal(Object v, int fallback) {
        if (v instanceof Number n) {
            return n.intValue();
        }
        try {
            return Integer.parseInt(stringVal(v));
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castSignalList(Object v) {
        if (v instanceof List<?> list) {
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

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castMapList(Object v) {
        if (v instanceof List<?> list) {
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
}
