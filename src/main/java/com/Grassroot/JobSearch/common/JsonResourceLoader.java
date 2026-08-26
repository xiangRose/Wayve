package com.Grassroot.JobSearch.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class JsonResourceLoader {

    private final ObjectMapper objectMapper;

    public JsonResourceLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T load(String classpath, Class<T> type) {
        try (InputStream in = new ClassPathResource(classpath).getInputStream()) {
            return objectMapper.readValue(in, type);
        } catch (IOException ex) {
            throw new IllegalStateException("无法读取: " + classpath, ex);
        }
    }

    public <T> T load(String classpath, TypeReference<T> type) {
        try (InputStream in = new ClassPathResource(classpath).getInputStream()) {
            return objectMapper.readValue(in, type);
        } catch (IOException ex) {
            throw new IllegalStateException("无法读取: " + classpath, ex);
        }
    }
}
