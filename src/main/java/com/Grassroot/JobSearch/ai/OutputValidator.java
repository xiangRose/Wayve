package com.Grassroot.JobSearch.ai;

import com.Grassroot.JobSearch.common.JsonResourceLoader;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class OutputValidator {

    private static final Pattern FORBIDDEN = Pattern.compile("最适合|天生适合|一定不适合|你就是");

    private final JsonResourceLoader jsonResourceLoader;

    public OutputValidator(JsonResourceLoader jsonResourceLoader) {
        this.jsonResourceLoader = jsonResourceLoader;
    }

    public void validateNoForbiddenWords(String text) {
        if (text != null && FORBIDDEN.matcher(text).find()) {
            throw new IllegalStateException("AI 输出含禁用词");
        }
    }

    public Map<String, Object> fallback(String name) {
        return jsonResourceLoader.load("AI/fallbacks/" + name + ".json", new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
    }
}
