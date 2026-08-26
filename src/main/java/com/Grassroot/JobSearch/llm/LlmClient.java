package com.Grassroot.JobSearch.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 大模型 HTTP 客户端 — Day 2 接入。
 * 配置 app.ai.enabled=true 并设置 AI_API_KEY 后实现 chat/completions 调用。
 */
@Component
public class LlmClient {

    private static final Logger log = LoggerFactory.getLogger(LlmClient.class);

    private final LlmProperties properties;

    public LlmClient(LlmProperties properties) {
        this.properties = properties;
    }

    public String chat(String systemPrompt, String userMessage) {
        if (!properties.isEnabled() || properties.getApiKey() == null || properties.getApiKey().isBlank()) {
            log.debug("LLM 未启用，跳过调用");
            return null;
        }
        // TODO Day 2: WebClient POST {baseUrl}/chat/completions
        log.warn("LlmClient.chat 尚未实现，请 Day 2 补充 WebClient 调用");
        return null;
    }
}
