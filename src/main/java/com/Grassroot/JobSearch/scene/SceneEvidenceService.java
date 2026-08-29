package com.Grassroot.JobSearch.scene;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Grassroot.JobSearch.ai.AiOrchestrator;
import com.Grassroot.JobSearch.common.ApiException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SceneEvidenceService {

    private final SceneCatalogService catalog;
    private final SceneEvidenceRepository repository;
    private final AiOrchestrator aiOrchestrator;
    private final ObjectMapper objectMapper;

    public SceneEvidenceService(
            SceneCatalogService catalog,
            SceneEvidenceRepository repository,
            AiOrchestrator aiOrchestrator,
            ObjectMapper objectMapper) {
        this.catalog = catalog;
        this.repository = repository;
        this.aiOrchestrator = aiOrchestrator;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> getSceneScript(String sceneId) {
        return catalog.getScene(sceneId);
    }

    public List<Map<String, Object>> listScenesForRole(String roleId) {
        return catalog.listScenesForRole(roleId);
    }

    public List<Map<String, Object>> listEvidence(String sessionId) {
        return repository.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(this::toMap)
                .toList();
    }

    @Transactional
    public Map<String, Object> submitAnswer(String sessionId, String sceneId, SceneAnswerRequest req) {
        Map<String, Object> scene = catalog.getScene(sceneId);
        String roleId = normalizeRoleId(req.roleId());
        SceneAnswerRequest normalized = new SceneAnswerRequest(
                roleId, req.answerType(), req.selectedOptionId(), req.rawAnswer());
        if (!roleId.equals(scene.get("roleId"))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "ROLE_SCENE_MISMATCH",
                    "岗位与场景不匹配，PRODUCT_S1 请用 roleId=ai_pm");
        }

        Map<String, Object> extracted = resolveEvidence(sceneId, scene, normalized);
        SceneEvidence record = buildRecord(sessionId, sceneId, scene, normalized, extracted);
        repository.save(record);
        return toMap(record);
    }

    private static String normalizeRoleId(String roleId) {
        if (roleId == null) {
            return "";
        }
        return switch (roleId) {
            case "ai_product" -> "ai_pm";
            case "ai_ui_design" -> "ai_ux";
            case "ai_ops" -> "ai_operator";
            case "ai_data_eval" -> "ai_researcher";
            case "ai_app_dev" -> "ai_consultant";
            default -> roleId;
        };
    }

    private Map<String, Object> resolveEvidence(String sceneId, Map<String, Object> scene, SceneAnswerRequest req) {
        String type = req.answerType().toLowerCase();
        if ("preset".equals(type)) {
            if (req.selectedOptionId() == null || req.selectedOptionId().isBlank()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "MISSING_OPTION", "固定选项需提供 selectedOptionId");
            }
            Map<String, Object> preset = catalog.getPreset(req.selectedOptionId());
            if (!sceneId.equals(preset.get("sceneId"))) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "OPTION_SCENE_MISMATCH", "选项与场景不匹配");
            }
            return preset;
        }
        if ("custom".equals(type)) {
            if (req.rawAnswer() == null || req.rawAnswer().isBlank()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "MISSING_ANSWER", "自由回答不能为空");
            }
            return aiOrchestrator.extractSceneEvidence(sceneId, req.roleId(), scene, req.rawAnswer());
        }
        throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_ANSWER_TYPE", "answerType 须为 preset 或 custom");
    }

    @SuppressWarnings("unchecked")
    private SceneEvidence buildRecord(
            String sessionId,
            String sceneId,
            Map<String, Object> scene,
            SceneAnswerRequest req,
            Map<String, Object> extracted) {
        SceneEvidence record = new SceneEvidence();
        record.setSessionId(sessionId);
        record.setSceneId(sceneId);
        record.setSceneType(String.valueOf(scene.getOrDefault("sceneType", "meeting")));
        record.setRoleId(req.roleId());
        record.setAnswerType(req.answerType().toLowerCase());
        record.setRawAnswer("preset".equalsIgnoreCase(req.answerType()) ? req.selectedOptionId() : req.rawAnswer());
        record.setObservedBehavior(stringVal(extracted.get("observedBehavior")));
        record.setEvidenceSummary(stringVal(extracted.get("evidenceSummary")));
        record.setConfidence(numberVal(extracted.get("confidence"), extracted.get("overall_confidence")));

        Object workstyle = extracted.get("workstyleEvidence");
        if (workstyle instanceof Map<?, ?> map) {
            record.setWorkstyleEvidence(objectMapper.convertValue(map, new TypeReference<>() {}));
        }
        Object tags = extracted.get("roleTags");
        if (tags instanceof List<?> list) {
            record.setRoleTags(list.stream().map(String::valueOf).toList());
        }
        return record;
    }

    private Map<String, Object> toMap(SceneEvidence record) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", record.getId());
        m.put("sessionId", record.getSessionId());
        m.put("sceneId", record.getSceneId());
        m.put("sceneType", record.getSceneType());
        m.put("roleId", record.getRoleId());
        m.put("answerType", record.getAnswerType());
        m.put("rawAnswer", record.getRawAnswer());
        m.put("observedBehavior", record.getObservedBehavior());
        m.put("workstyleEvidence", record.getWorkstyleEvidence());
        m.put("roleTags", record.getRoleTags());
        m.put("evidenceSummary", record.getEvidenceSummary());
        m.put("confidence", record.getConfidence());
        m.put("createdAt", record.getCreatedAt());
        m.put("boundaryNotice", "本结果为单幕会议室场景下的行为记录，不代表岗位适配或能力结论。三幕汇总后方可生成跨场景规律。");
        return m;
    }

    private static String stringVal(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    private static double numberVal(Object primary, Object fallback) {
        Object v = primary != null ? primary : fallback;
        if (v instanceof Number n) {
            return n.doubleValue();
        }
        try {
            return v == null ? 0.0 : Double.parseDouble(String.valueOf(v));
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }
}
