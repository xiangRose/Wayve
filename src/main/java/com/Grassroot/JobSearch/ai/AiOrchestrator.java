package com.Grassroot.JobSearch.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Grassroot.JobSearch.llm.LlmClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * AI 编排入口 — 对应 PRD 六大模块。
 * Prompt 文件放在 {@code AI/prompts/}（项目根目录）。
 */
@Service
public class AiOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(AiOrchestrator.class);

    private final LlmClient llmClient;
    private final PromptLoader promptLoader;
    private final OutputValidator outputValidator;
    private final ObjectMapper objectMapper;

    public AiOrchestrator(
            LlmClient llmClient,
            PromptLoader promptLoader,
            OutputValidator outputValidator,
            ObjectMapper objectMapper) {
        this.llmClient = llmClient;
        this.promptLoader = promptLoader;
        this.outputValidator = outputValidator;
        this.objectMapper = objectMapper;
    }

    /** 模块1：背景证据提取 */
    public Map<String, Object> extractResumeEvidence(String backgroundText, String userStage) {
        String prompt = promptLoader.load("01-resume-extraction");
        Map<String, Object> payload = Map.of(
                "user_stage", userStage == null ? "beginner" : userStage,
                "background_text", backgroundText == null ? "" : backgroundText);
        return chatJsonMap(prompt, payload, "resume-extraction");
    }

    /** 模块2：候选岗位推荐 */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> recommendJobs(Map<String, Object> context) {
        String prompt = promptLoader.load("02-job-recommendation");
        Map<String, Object> result = chatJsonMap(prompt, context, "job-recommendation");
        Object recs = result.get("recommendations");
        if (recs instanceof List<?> list) {
            return (List<Map<String, Object>>) list;
        }
        return List.of();
    }

    /** 模块6：报告文字生成（差距分析 + 行动任务） */
    public Map<String, Object> generateReport(Map<String, Object> context) {
        String prompt = promptLoader.load("06-report-generation");
        return chatJsonMap(prompt, context, "report-generation", true);
    }

    /** 专项：根据题目选择与情景行为生成行为信号（结构化） */
    public Map<String, Object> generateBehaviorSignals(Map<String, Object> context) {
        String prompt = promptLoader.load("07-judgment-basis");
        Map<String, Object> payload = new HashMap<>();
        payload.put("microtask_choice_signals", context.getOrDefault("microtask_choice_signals", List.of()));
        payload.put("microtask_capability_summary", context.getOrDefault("microtask_capability_summary", List.of()));
        payload.put("scene_evidences", context.getOrDefault("scene_evidences", List.of()));
        payload.put("user_subjective_highlights", context.getOrDefault("user_subjective_highlights", List.of()));
        payload.put("selected_target_job", context.getOrDefault("selected_target_job", ""));
        try {
            Map<String, Object> result = chatJsonMap(prompt, payload, "judgment-basis", false, true);
            if (result.get("topSignals") instanceof List<?> || result.get("allEvidence") instanceof List<?>) {
                return result;
            }
            return Map.of();
        } catch (Exception ex) {
            log.warn("AI 行为信号生成失败: {}", ex.getMessage());
            return Map.of();
        }
    }

    /** 专项：根据题目选择与情景行为生成 AI 判断依据（不含分数） */
    public List<String> generateJudgmentBasis(Map<String, Object> context) {
        Map<String, Object> pack = generateBehaviorSignals(context);
        List<Map<String, Object>> all = castMapList(pack.get("allEvidence"));
        List<String> lines = new ArrayList<>();
        for (Map<String, Object> signal : all) {
            lines.add(flattenSignal(signal));
        }
        return lines;
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

    private String stringVal(Object v) {
        return v == null ? "" : String.valueOf(v).trim();
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

    /**
     * 模块3：会议室场景 · 自定义回答（C 选项）证据提取。
     * A/B 固定选项不经过此路径。
     */
    public Map<String, Object> extractSceneEvidence(
            String sceneId,
            String roleId,
            Map<String, Object> scene,
            String userAnswer) {
        String prompt = promptLoader.load("03-scene-evidence-extraction");
        Map<String, Object> payload = new HashMap<>();
        payload.put("scene_id", sceneId);
        payload.put("role_id", roleId);
        payload.put("scene_context", scene.get("context"));
        payload.put("scene_question", scene.get("question"));
        payload.put("user_answer", userAnswer);

        try {
            Map<String, Object> parsed = chatJsonMap(prompt, payload, "scene-evidence-custom");
            if (!parsed.containsKey("confidence") && parsed.containsKey("overall_confidence")) {
                parsed.put("confidence", parsed.get("overall_confidence"));
            }
            return parsed;
        } catch (Exception ex) {
            log.warn("自定义场景证据提取失败，使用 fallback: {}", ex.getMessage());
            return outputValidator.fallback("scene-evidence-custom");
        }
    }

    private Map<String, Object> chatJsonMap(String prompt, Object payload, String fallbackName) {
        return chatJsonMap(prompt, payload, fallbackName, false, false);
    }

    private Map<String, Object> chatJsonMap(
            String prompt, Object payload, String fallbackName, boolean reportOutput) {
        return chatJsonMap(prompt, payload, fallbackName, reportOutput, false);
    }

    private Map<String, Object> chatJsonMap(
            String prompt,
            Object payload,
            String fallbackName,
            boolean reportOutput,
            boolean judgmentBasisOutput) {
        try {
            String userJson = payload instanceof String ? (String) payload : objectMapper.writeValueAsString(payload);
            String raw = llmClient.chatJson(prompt, userJson);
            if (raw == null || raw.isBlank()) {
                log.warn("LLM 未返回，使用 fallback: {}", fallbackName);
                return outputValidator.fallback(fallbackName);
            }
            if (judgmentBasisOutput) {
                outputValidator.validateJudgmentBasisOutput(raw);
            } else if (reportOutput) {
                outputValidator.validateReportOutput(raw);
            } else {
                outputValidator.validateNoForbiddenWords(raw);
            }
            return objectMapper.readValue(raw, new TypeReference<>() {});
        } catch (Exception ex) {
            log.warn("AI 调用失败，使用 fallback {}: {}", fallbackName, ex.getMessage());
            return outputValidator.fallback(fallbackName);
        }
    }

    @SuppressWarnings("unchecked")
    private List<String> castStringList(Object v) {
        if (v instanceof List<?> list) {
            List<String> out = new ArrayList<>();
            for (Object item : list) {
                if (item != null && !String.valueOf(item).isBlank()) {
                    out.add(String.valueOf(item).trim());
                }
            }
            return out;
        }
        return List.of();
    }
}
