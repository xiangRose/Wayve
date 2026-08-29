package com.Grassroot.JobSearch.session;

import com.Grassroot.JobSearch.common.enums.ClarityLevel;
import com.Grassroot.JobSearch.common.enums.CurrentStatus;
import com.Grassroot.JobSearch.common.enums.UserStage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
public class UserSession {

    @Id
    @Column(name = "session_id", length = 36)
    private String sessionId;

    @Enumerated(EnumType.STRING)
    private UserStage userStage;

    @Enumerated(EnumType.STRING)
    private ClarityLevel clarityLevel;

    @Enumerated(EnumType.STRING)
    private CurrentStatus currentStatus;

    private String education;

    @Column(length = 4000)
    private String backgroundText;

    @Column(length = 4000)
    private String teamRoleDescription;

    @Column(length = 2000)
    private String workPreference;

    @Column(length = 8000)
    private String resumeText;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resume_evidence_data")
    private Map<String, Object> resumeEvidenceData;

    private String resumeEvidenceStatus = "pending";

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public String getSessionId() { return sessionId; }
    public UserStage getUserStage() { return userStage; }
    public void setUserStage(UserStage userStage) { this.userStage = userStage; }
    public ClarityLevel getClarityLevel() { return clarityLevel; }
    public void setClarityLevel(ClarityLevel clarityLevel) { this.clarityLevel = clarityLevel; }
    public CurrentStatus getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(CurrentStatus currentStatus) { this.currentStatus = currentStatus; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getBackgroundText() { return backgroundText; }
    public void setBackgroundText(String backgroundText) { this.backgroundText = backgroundText; }
    public String getTeamRoleDescription() { return teamRoleDescription; }
    public void setTeamRoleDescription(String teamRoleDescription) { this.teamRoleDescription = teamRoleDescription; }
    public String getWorkPreference() { return workPreference; }
    public void setWorkPreference(String workPreference) { this.workPreference = workPreference; }
    public String getResumeText() { return resumeText; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }
    public Map<String, Object> getResumeEvidenceData() { return resumeEvidenceData; }
    public void setResumeEvidenceData(Map<String, Object> resumeEvidenceData) { this.resumeEvidenceData = resumeEvidenceData; }
    public String getResumeEvidenceStatus() { return resumeEvidenceStatus; }
    public void setResumeEvidenceStatus(String resumeEvidenceStatus) { this.resumeEvidenceStatus = resumeEvidenceStatus; }
    public Instant getCreatedAt() { return createdAt; }
}
