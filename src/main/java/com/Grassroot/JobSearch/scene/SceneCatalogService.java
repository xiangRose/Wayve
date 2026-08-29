package com.Grassroot.JobSearch.scene;

import com.fasterxml.jackson.core.type.TypeReference;
import com.Grassroot.JobSearch.common.JsonResourceLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SceneCatalogService {

    private final Map<String, Map<String, Object>> scenes;
    private final Map<String, Map<String, Object>> presets;

    public SceneCatalogService(JsonResourceLoader json) {
        Map<String, Map<String, Object>> merged = new HashMap<>();
        merged.putAll(json.load("seed/scene-scripts/s1-meeting.json", new TypeReference<>() {}));
        merged.putAll(json.load("seed/scene-scripts/s2-client.json", new TypeReference<>() {}));
        merged.putAll(json.load("seed/scene-scripts/s3-release.json", new TypeReference<>() {}));
        this.scenes = merged;
        this.presets = json.load("seed/scene-evidence/presets.json", new TypeReference<>() {});
    }

    public Map<String, Object> getScene(String sceneId) {
        Map<String, Object> scene = scenes.get(sceneId);
        if (scene == null) {
            throw new IllegalArgumentException("未知场景: " + sceneId);
        }
        return shuffleOptions(scene);
    }

    public Map<String, Object> getPreset(String optionId) {
        Map<String, Object> preset = presets.get(optionId);
        if (preset == null) {
            throw new IllegalArgumentException("未知选项: " + optionId);
        }
        return preset;
    }

    public List<Map<String, Object>> listScenesForRole(String roleId) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> scene : scenes.values()) {
            if (roleId.equals(scene.get("roleId"))) {
                list.add(Map.of(
                        "sceneId", scene.get("sceneId"),
                        "title", scene.get("title"),
                        "time", scene.get("time")));
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> shuffleOptions(Map<String, Object> scene) {
        Map<String, Object> copy = new HashMap<>(scene);
        Object options = scene.get("options");
        if (options instanceof List<?> raw) {
            List<Map<String, Object>> shuffled = new ArrayList<>();
            for (Object item : raw) {
                shuffled.add(new HashMap<>((Map<String, Object>) item));
            }
            Collections.shuffle(shuffled);
            copy.put("options", shuffled);
        }
        return copy;
    }
}
