package com.Grassroot.JobSearch.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.Grassroot.JobSearch.common.JsonResourceLoader;
import com.Grassroot.JobSearch.llm.LlmClient;
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

    public AiOrchestrator(LlmClient llmClient, PromptLoader promptLoader, OutputValidator outputValidator) {
        this.llmClient = llmClient;
        this.promptLoader = promptLoader;
        this.outputValidator = outputValidator;
    }

    /** 模块1：背景证据提取（异步占位） */
    public void extractResumeEvidenceAsync(String sessionId, String backgroundText, String userStage) {
        log.info("异步履历提取 sessionId={} stage={}", sessionId, userStage);
        String prompt = promptLoader.load("01-resume-extraction");
        String raw = llmClient.chat(prompt, backgroundText == null ? "" : backgroundText);
        if (raw == null) {
            log.debug("使用 fallback: resume-extraction.json");
        } else {
            outputValidator.validateNoForbiddenWords(raw);
        }
    }

    /** 模块2：候选岗位推荐 */
    public List<Map<String, Object>> recommendJobs(Map<String, Object> context) {
        String prompt = promptLoader.load("02-job-recommendation");
        llmClient.chat(prompt, String.valueOf(context));
        return List.of();
    }

    /** 模块4+6：行为证据 + 报告（Day 2） */
    public Map<String, Object> generateReport(Map<String, Object> context) {
        String prompt = promptLoader.load("06-report-generation");
        llmClient.chat(prompt, String.valueOf(context));
        return Map.of();
    }
}
