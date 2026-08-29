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

    public Map<String, Object> fallback(String name) {
        return jsonResourceLoader.load("AI/fallbacks/" + name + ".json", new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
    }
}
