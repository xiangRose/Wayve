package com.Grassroot.JobSearch.session;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import com.Grassroot.JobSearch.config.AppConstants;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/v1")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/sessions")
    public Map<String, Object> create() {
        UserSession session = sessionService.create();
        return Map.of("sessionId", session.getSessionId(), "createdAt", session.getCreatedAt().toString());
    }

    @PutMapping("/sessions/{sessionId}/profile")
    public Map<String, Object> profile(@PathVariable String sessionId, @Valid @RequestBody ProfileRequest req) {
        return sessionService.updateProfile(sessionId, req);
    }

    @DeleteMapping("/sessions/{sessionId}/data")
    public Map<String, Object> delete(@PathVariable String sessionId) {
        sessionService.deleteAll(sessionId);
        return Map.of("ok", true);
    }

    @GetMapping("/resume/evidence")
    public Map<String, Object> resumeEvidence(
            @RequestHeader(value = AppConstants.HEADER_SESSION_ID, required = false) String sessionId) {
        return sessionService.resumeEvidence(sessionId);
    }
}
