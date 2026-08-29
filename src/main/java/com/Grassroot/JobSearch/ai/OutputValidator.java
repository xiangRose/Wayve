package com.Grassroot.JobSearch.ai;

import com.Grassroot.JobSearch.common.JsonResourceLoader;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class OutputValidator {

    private static final Pattern FORBIDDEN = Pattern.compile("最适合|天生适合|一定不适合|你就是");
  private static final Pattern SCENE_FORBIDDEN = Pattern.compile(
            "很适合|抗压能力很强|很有责任心|优秀决定|沟通能力很好|你很适合|你是一个");
    private static final Pattern REPORT_FORBIDDEN = Pattern.compile(
            "适配潜力|\\d+\\s*分|得分|分数|雷达图|维度.*分|能力.*(很强|较弱|较高|较低|突出|不足)"
                    + "|表现较好|表现最好|表现较差|表现较弱|你的优势是|你.*能力强|你.*能力弱|潜力高|潜力低");

    private final JsonResourceLoader jsonResourceLoader;

    public OutputValidator(JsonResourceLoader jsonResourceLoader) {
        this.jsonResourceLoader = jsonResourceLoader;
    }

    public void validateNoForbiddenWords(String text) {
        if (text != null && FORBIDDEN.matcher(text).find()) {
            throw new IllegalStateException("AI 输出含禁用词");
        }
    }

    public void validateSceneEvidenceOutput(String text) {
        validateNoForbiddenWords(text);
        if (text != null && SCENE_FORBIDDEN.matcher(text).find()) {
            throw new IllegalStateException("AI 输出含场景禁用推断词");
        }
    }

    public void validateReportOutput(String text) {
        validateNoForbiddenWords(text);
        if (text != null && REPORT_FORBIDDEN.matcher(text).find()) {
            throw new IllegalStateException("AI 报告输出含禁用定性或分数表述");
        }
    }

    public void validateJudgmentBasisOutput(String text) {
        validateNoForbiddenWords(text);
        if (text != null && REPORT_FORBIDDEN.matcher(text).find()) {
            throw new IllegalStateException("AI 判断依据含禁用定性或分数表述");
        }
    }

    public Map<String, Object> fallback(String name) {
        return jsonResourceLoader.load("AI/fallbacks/" + name + ".json", new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
    }
}
