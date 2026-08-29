package com.Grassroot.JobSearch.ai;

import com.Grassroot.JobSearch.common.JobIdMapper;
import com.Grassroot.JobSearch.scene.SceneEvidence;
import com.Grassroot.JobSearch.scene.SceneEvidenceRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * 根据微任务与情景的客观选择记录，生成结合题目的判断依据（不含分数与能力定论）。
 */
@Component
public class JudgmentBasisComposer {

    private final ReportContextBuilder reportContextBuilder;
    private final SceneEvidenceRepository sceneEvidenceRepository;

    public JudgmentBasisComposer(
            ReportContextBuilder reportContextBuilder,
            SceneEvidenceRepository sceneEvidenceRepository) {
        this.reportContextBuilder = reportContextBuilder;
        this.sceneEvidenceRepository = sceneEvidenceRepository;
    }

    public List<String> compose(String sessionId, String backendJobId) {
        List<String> lines = new ArrayList<>();
        List<Map<String, Object>> signals =
                reportContextBuilder.buildMicrotaskChoiceSignals(sessionId, backendJobId);
        for (Map<String, Object> signal : signals) {
            lines.add(formatMicrotaskLine(signal));
        }
        for (SceneEvidence ev : sceneEvidenceRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)) {
            if (backendJobId != null && !backendJobId.isBlank() && !matchesRole(ev.getRoleId(), backendJobId)) {
                continue;
            }
            String sceneLine = formatSceneLine(ev);
            if (sceneLine != null) {
                lines.add(sceneLine);
            }
        }
        return lines;
    }

    private String formatMicrotaskLine(Map<String, Object> signal) {
        String dimension = stringVal(signal.get("dimension"));
        String who = formatWho(signal);
        String scenario = clip(stringVal(signal.get("scenario")), 72);
        String question = clip(stringVal(signal.get("prompt")), 72);
        String choice = clip(stringVal(signal.get("selectedOption")), 56);
        String contrast = formatContrast(signal);
        String tag = dimension.isBlank() ? "" : "【" + dimension + "】";

        return tag + who + "在「" + scenario + "」的情境里，面对「" + question + "」，你选择了「" + choice + "」。"
                + contrast;
    }

    @SuppressWarnings("unchecked")
    private String formatContrast(Map<String, Object> signal) {
        Object othersObj = signal.get("otherOptions");
        if (!(othersObj instanceof List<?> list) || list.isEmpty()) {
            return "这一选择体现了你在该题里优先关注的判断线索。";
        }
        List<String> others = new ArrayList<>();
        for (Object item : list) {
            if (item != null && !String.valueOf(item).isBlank()) {
                others.add(clip(String.valueOf(item), 40));
            }
        }
        if (others.isEmpty()) {
            return "这一选择体现了你在该题里优先关注的判断线索。";
        }
        if (others.size() == 1) {
            return "相比「" + others.get(0) + "」等方向，你更先把注意力放在当前选项所代表的路径上。";
        }
        return "相比「" + others.get(0) + "」「" + others.get(1) + "」等备选，你更先把注意力放在当前选项所代表的路径上。";
    }

    private String formatWho(Map<String, Object> signal) {
        String speaker = stringVal(signal.get("speaker"));
        String role = stringVal(signal.get("speakerRole"));
        String time = stringVal(signal.get("time"));
        if (!role.isBlank()) {
            return speaker + "（" + role + "）" + (time.isBlank() ? "" : " " + time);
        }
        return speaker.isBlank() ? "题目情境" : speaker;
    }

    private String formatSceneLine(SceneEvidence ev) {
        String behavior = ev.getObservedBehavior();
        String summary = ev.getEvidenceSummary();
        if (behavior == null || behavior.isBlank()) {
            if (summary == null || summary.isBlank()) {
                return null;
            }
            return "【情景模拟·" + sceneLabel(ev.getSceneId()) + "】" + clip(summary, 120) + "。";
        }
        return "【情景模拟·" + sceneLabel(ev.getSceneId()) + "】"
                + clip(behavior, 100)
                + (summary == null || summary.isBlank() ? "" : "（" + clip(summary, 60) + "）")
                + "。";
    }

    private static boolean matchesRole(String storedRoleId, String backendJobId) {
        if (storedRoleId == null || backendJobId == null) {
            return false;
        }
        return backendJobId.equals(storedRoleId) || backendJobId.equals(JobIdMapper.toBackend(storedRoleId));
    }

    private static String sceneLabel(String sceneId) {
        if (sceneId == null) {
            return "现场";
        }
        return switch (sceneId) {
            case "PRODUCT_S1", "UI_S1", "OPS_S1", "DATA_S1", "DEV_S1" -> "项目会议室";
            case "PRODUCT_S2", "UI_S2", "OPS_S2", "DATA_S2", "DEV_S2" -> "客户沟通";
            case "PRODUCT_S3", "UI_S3", "OPS_S3", "DATA_S3", "DEV_S3" -> "发布现场";
            default -> sceneId;
        };
    }

    private static String stringVal(Object v) {
        return v == null ? "" : String.valueOf(v).trim();
    }

    private static String clip(String text, int max) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String t = text.trim();
        return t.length() <= max ? t : t.substring(0, max - 1) + "…";
    }
}
