package com.Grassroot.JobSearch.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.Grassroot.JobSearch.common.ApiException;
import com.Grassroot.JobSearch.job.JobModel;
import com.Grassroot.JobSearch.job.JobRepository;
import com.Grassroot.JobSearch.llm.LlmClient;
import com.Grassroot.JobSearch.llm.LlmException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/** 读取 research/raw 样本，调用 LLM 归纳岗位模型。 */
@Service
public class JobResearchService {

    private final LlmClient llmClient;
    private final ObjectMapper objectMapper;
    private final JobRepository jobRepository;
    private final Path researchDir;

    public JobResearchService(
            LlmClient llmClient,
            ObjectMapper objectMapper,
            JobRepository jobRepository,
            @Value("${app.research-dir:research}") String researchDir) {
        this.llmClient = llmClient;
        this.objectMapper = objectMapper;
        this.jobRepository = jobRepository;
        this.researchDir = Path.of(researchDir);
    }

    public Map<String, Object> aggregateJob(String jobId, boolean dryRun) {
        JobModel job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "JOB_NOT_FOUND", "未知岗位: " + jobId));

        List<JsonNode> samples = loadSamples(jobId);
        if (samples.size() < 3) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "INSUFFICIENT_SAMPLES",
                    "样本不足（当前 " + samples.size() + "），建议至少 5 条再归纳");
        }

        String systemPrompt = loadResearchPrompt();
        String userMessage = buildUserMessage(jobId, job.getName(), samples);

        if (dryRun) {
            return Map.of(
                    "jobId", jobId,
                    "sampleCount", samples.size(),
                    "previewChars", Math.min(userMessage.length(), 2000),
                    "userMessagePreview", userMessage.substring(0, Math.min(userMessage.length(), 2000)));
        }

        if (!llmClient.isReady()) {
            throw new ApiException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "LLM_NOT_READY",
                    "请设置 AI_API_KEY 并将 app.ai.enabled=true");
        }

        String rawJson = llmClient.chatJson(systemPrompt, userMessage);
        if (rawJson == null || rawJson.isBlank()) {
            throw new LlmException("模型未返回内容");
        }

        try {
            JsonNode result = objectMapper.readTree(rawJson);
            Path outDir = researchDir.resolve("aggregated");
            Files.createDirectories(outDir);
            Path outFile = outDir.resolve(jobId + ".summary.json");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outFile.toFile(), result);
            return Map.of(
                    "jobId", jobId,
                    "sampleCount", samples.size(),
                    "outputFile", outFile.toAbsolutePath().toString(),
                    "summary", objectMapper.convertValue(result, Map.class));
        } catch (IOException ex) {
            throw new IllegalStateException("保存归纳结果失败", ex);
        }
    }

    private List<JsonNode> loadSamples(String jobId) {
        Path jobDir = researchDir.resolve("raw").resolve(jobId);
        if (!Files.isDirectory(jobDir)) {
            return List.of();
        }
        List<JsonNode> samples = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(jobDir, "*.json")) {
            for (Path path : stream) {
                samples.add(objectMapper.readTree(path.toFile()));
            }
        } catch (IOException ex) {
            throw new IllegalStateException("读取样本失败: " + jobDir, ex);
        }
        samples.sort(Comparator.comparing(n -> n.path("sampleId").asText("")));
        return samples;
    }

    private String loadResearchPrompt() {
        Path promptFile = researchDir.resolve("prompts").resolve("03-jd-aggregation.md");
        if (!Files.exists(promptFile)) {
            return "你是岗位研究分析师，请将 JD 样本归纳为 JSON 岗位模型。";
        }
        try {
            return Files.readString(promptFile, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("无法读取 Prompt: " + promptFile, ex);
        }
    }

    private String buildUserMessage(String jobId, String jobName, List<JsonNode> samples) {
        StringBuilder sb = new StringBuilder();
        sb.append("岗位: ").append(jobName).append(" (").append(jobId).append(")\n");
        sb.append("样本数: ").append(samples.size()).append("\n\n");
        int index = 1;
        for (JsonNode sample : samples) {
            sb.append("--- 样本 ").append(index++).append(" [")
                    .append(sample.path("source").asText(""))
                    .append("] ")
                    .append(sample.path("title").asText(""))
                    .append(" ---\n");
            sb.append("来源: ").append(sample.path("sourceUrl").asText("无")).append("\n");
            String raw = sample.path("rawText").asText("");
            sb.append(raw, 0, Math.min(raw.length(), 4000)).append("\n\n");
        }
        return sb.toString();
    }
}
