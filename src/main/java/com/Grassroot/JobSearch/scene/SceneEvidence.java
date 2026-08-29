package com.Grassroot.JobSearch.scene;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "scene_evidence")
public class SceneEvidence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "scene_id", nullable = false)
    private String sceneId;

    @Column(name = "scene_type")
    private String sceneType = "meeting";

    @Column(name = "role_id", nullable = false)
    private String roleId;

    @Column(name = "answer_type", nullable = false)
    private String answerType;

    @Column(name = "raw_answer", columnDefinition = "TEXT")
    private String rawAnswer;

    @Column(name = "observed_behavior", columnDefinition = "TEXT")
    private String observedBehavior;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "workstyle_evidence")
    private Map<String, Object> workstyleEvidence = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "role_tags")
    private List<String> roleTags = new ArrayList<>();

    @Column(name = "evidence_summary", columnDefinition = "TEXT")
    private String evidenceSummary;

    private double confidence;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    public String getId() { return id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public String getSceneType() { return sceneType; }
    public void setSceneType(String sceneType) { this.sceneType = sceneType; }
    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
    public String getAnswerType() { return answerType; }
    public void setAnswerType(String answerType) { this.answerType = answerType; }
    public String getRawAnswer() { return rawAnswer; }
    public void setRawAnswer(String rawAnswer) { this.rawAnswer = rawAnswer; }
    public String getObservedBehavior() { return observedBehavior; }
    public void setObservedBehavior(String observedBehavior) { this.observedBehavior = observedBehavior; }
    public Map<String, Object> getWorkstyleEvidence() { return workstyleEvidence; }
    public void setWorkstyleEvidence(Map<String, Object> workstyleEvidence) { this.workstyleEvidence = workstyleEvidence; }
    public List<String> getRoleTags() { return roleTags; }
    public void setRoleTags(List<String> roleTags) { this.roleTags = roleTags; }
    public String getEvidenceSummary() { return evidenceSummary; }
    public void setEvidenceSummary(String evidenceSummary) { this.evidenceSummary = evidenceSummary; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public Instant getCreatedAt() { return createdAt; }
}
