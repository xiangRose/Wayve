package com.Grassroot.JobSearch.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PromptLoader {

    private final Path promptsDir;

    public PromptLoader(@Value("${app.prompts-dir:AI/prompts}") String promptsDir) {
        this.promptsDir = Path.of(promptsDir);
    }

    public String load(String moduleName) {
        Path file = promptsDir.resolve(moduleName + ".md");
        if (!Files.exists(file)) {
            return "你是职业探索助手。模块: " + moduleName;
        }
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("无法读取 Prompt: " + file, ex);
        }
    }
}
