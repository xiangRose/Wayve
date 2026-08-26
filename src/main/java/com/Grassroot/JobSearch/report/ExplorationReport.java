package com.Grassroot.JobSearch.report;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "exploration_reports")
public class ExplorationReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "selected_target_job")
    private String selectedTargetJob;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resume_radar_data")
    private Map<String, Object> resumeRadarData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "task_evidence_summary")
    private Map<String, Object> taskEvidenceSummary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "interest_signals")
    private List<Map<String, Object>> interestSignals;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gap_analysis")
    private Map<String, Object> gapAnalysis;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "action_tasks")
    private List<Map<String, Object>> actionTasks;

    @Column(length = 2000)
    private String comparisonSummary;

    @Column(length = 2000)
    private String boundaryNotice;

    private Instant generatedAt;

    @PrePersist
    void prePersist() { generatedAt = Instant.now(); }

    public String getId() { return id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getSelectedTargetJob() { return selectedTargetJob; }
    public void setSelectedTargetJob(String selectedTargetJob) { this.selectedTargetJob = selectedTargetJob; }
    public Map<String, Object> getResumeRadarData() { return resumeRadarData; }
    public void setResumeRadarData(Map<String, Object> resumeRadarData) { this.resumeRadarData = resumeRadarData; }
    public Map<String, Object> getTaskEvidenceSummary() { return taskEvidenceSummary; }
    public void setTaskEvidenceSummary(Map<String, Object> taskEvidenceSummary) { this.taskEvidenceSummary = taskEvidenceSummary; }
    public List<Map<String, Object>> getInterestSignals() { return interestSignals; }
    public void setInterestSignals(List<Map<String, Object>> interestSignals) { this.interestSignals = interestSignals; }
    public Map<String, Object> getGapAnalysis() { return gapAnalysis; }
    public void setGapAnalysis(Map<String, Object> gapAnalysis) { this.gapAnalysis = gapAnalysis; }
    public List<Map<String, Object>> getActionTasks() { return actionTasks; }
    public void setActionTasks(List<Map<String, Object>> actionTasks) { this.actionTasks = actionTasks; }
    public String getComparisonSummary() { return comparisonSummary; }
    public void setComparisonSummary(String comparisonSummary) { this.comparisonSummary = comparisonSummary; }
    public String getBoundaryNotice() { return boundaryNotice; }
    public void setBoundaryNotice(String boundaryNotice) { this.boundaryNotice = boundaryNotice; }
}
