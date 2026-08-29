package com.Grassroot.JobSearch.session;

import com.Grassroot.JobSearch.ai.AiOrchestrator;
import com.Grassroot.JobSearch.common.ApiException;
import com.Grassroot.JobSearch.common.enums.ClarityLevel;
import com.Grassroot.JobSearch.common.enums.CurrentStatus;
import com.Grassroot.JobSearch.common.enums.UserStage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionService {

    private final UserSessionRepository repository;
    private final AiOrchestrator aiOrchestrator;

    public SessionService(UserSessionRepository repository, AiOrchestrator aiOrchestrator) {
        this.repository = repository;
        this.aiOrchestrator = aiOrchestrator;
    }

    @Transactional
    public UserSession create() {
        return repository.save(new UserSession());
    }

    @Transactional
    public Map<String, Object> updateProfile(String sessionId, ProfileRequest req) {
        UserSession session = find(sessionId);
        session.setUserStage(UserStage.valueOf(req.userStage()));
        session.setClarityLevel(ClarityLevel.valueOf(req.clarityLevel()));
        session.setCurrentStatus(CurrentStatus.valueOf(req.currentStatus()));
        session.setEducation(req.education());
        session.setBackgroundText(req.backgroundText());
        session.setTeamRoleDescription(req.teamRoleDescription());
        session.setWorkPreference(req.workPreference());
        session.setResumeText(req.resumeText());
        session.setResumeEvidenceStatus("processing");
        repository.save(session);

        Map<String, Object> evidence = aiOrchestrator.extractResumeEvidence(req.backgroundText(), req.userStage());
        session.setResumeEvidenceData(evidence);
        session.setResumeEvidenceStatus("fallback".equals(evidence.get("status")) ? "fallback" : "ready");
        repository.save(session);

        return Map.of("ok", true, "message", "资料已保存，履历证据已生成", "resumeEvidenceStatus", session.getResumeEvidenceStatus());
    }

    @Transactional
    public void deleteAll(String sessionId) {
        repository.deleteById(sessionId);
    }

    public UserSession find(String sessionId) {
        return repository.findById(sessionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "SESSION_NOT_FOUND", "会话不存在"));
    }

    public Map<String, Object> resumeEvidence(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "SESSION_REQUIRED", "需要 X-Session-Id");
        }
        UserSession session = find(sessionId);
        Map<String, Object> res = new HashMap<>();
        String status = session.getResumeEvidenceStatus();
        res.put("status", status == null || status.isBlank() ? "pending" : status);
        res.put("evidences", session.getResumeEvidenceData() == null ? Map.of() : session.getResumeEvidenceData());
        return res;
    }
}
