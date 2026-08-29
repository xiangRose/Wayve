package com.Grassroot.JobSearch.scene;

import com.Grassroot.JobSearch.config.AppConstants;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SceneController {

    private final SceneEvidenceService sceneEvidenceService;

    public SceneController(SceneEvidenceService sceneEvidenceService) {
        this.sceneEvidenceService = sceneEvidenceService;
    }

    @GetMapping("/scenes/{sceneId}")
    public Map<String, Object> getScene(@PathVariable String sceneId) {
        return sceneEvidenceService.getSceneScript(sceneId);
    }

    @GetMapping("/scenes")
    public Map<String, Object> listByRole(@RequestParam String roleId) {
        return Map.of("scenes", sceneEvidenceService.listScenesForRole(roleId));
    }

    @PostMapping("/scenes/{sceneId}/answers")
    public Map<String, Object> submitAnswer(
            @RequestHeader(AppConstants.HEADER_SESSION_ID) String sessionId,
            @PathVariable String sceneId,
            @Valid @RequestBody SceneAnswerRequest request) {
        return sceneEvidenceService.submitAnswer(sessionId, sceneId, request);
    }

    @GetMapping("/scene-evidence")
    public Map<String, Object> listEvidence(@RequestHeader(AppConstants.HEADER_SESSION_ID) String sessionId) {
        List<Map<String, Object>> items = sceneEvidenceService.listEvidence(sessionId);
        return Map.of("items", items);
    }
}
