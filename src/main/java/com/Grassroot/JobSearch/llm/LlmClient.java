package com.Grassroot.JobSearch.llm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

/**
 * OpenAI 兼容 Chat Completions 客户端（豆包 / DeepSeek / OpenAI 等均可）。
 */
@Component
public class LlmClient {

    private static final Logger log = LoggerFactory.getLogger(LlmClient.class);

    private final LlmProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public LlmClient(LlmProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(properties.getTimeoutMs()));
        factory.setReadTimeout(Duration.ofMillis(properties.getTimeoutMs()));
        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .build();
    }

    public boolean isReady() {
        return properties.isEnabled()
                && properties.getApiKey() != null
                && !properties.getApiKey().isBlank();
    }

    public String chat(String systemPrompt, String userMessage) {
        return complete(systemPrompt, userMessage, false);
    }

    public String chatJson(String systemPrompt, String userMessage) {
        return complete(systemPrompt, userMessage, true);
    }

    private String complete(String systemPrompt, String userMessage, boolean jsonMode) {
        if (!isReady()) {
            log.debug("LLM 未启用或未配置 API Key");
            return null;
        }
        try {
            ObjectNode body = objectMapper.createObjectNode();
            body.put("model", properties.getModelPro());
            body.put("temperature", 0.3);
            ArrayNode messages = body.putArray("messages");
            messages.addObject().put("role", "system").put("content", systemPrompt);
            messages.addObject().put("role", "user").put("content", userMessage);
            if (jsonMode) {
                body.putObject("response_format").put("type", "json_object");
            }

            String baseUrl = trimTrailingSlash(properties.getBaseUrl());
            String response = restClient.post()
                    .uri(baseUrl + "/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getApiKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            return extractContent(response);
        } catch (RestClientResponseException ex) {
            log.error("LLM HTTP {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new LlmException("模型接口返回错误: " + ex.getStatusCode());
        } catch (RestClientException ex) {
            log.error("LLM 调用失败: {}", ex.getMessage());
            throw new LlmException("模型接口调用失败: " + ex.getMessage());
        }
    }

    private String extractContent(String response) {
        if (response == null || response.isBlank()) {
            throw new LlmException("模型返回空响应");
        }
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || content.asText().isBlank()) {
                throw new LlmException("模型响应缺少 content 字段");
            }
            return content.asText();
        } catch (LlmException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new LlmException("无法解析模型响应: " + ex.getMessage());
        }
    }

    private static String trimTrailingSlash(String url) {
        if (url == null || url.isBlank()) {
            return "https://api.openai.com/v1";
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
