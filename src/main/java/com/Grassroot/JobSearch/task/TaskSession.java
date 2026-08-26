package com.Grassroot.JobSearch.task;

import com.Grassroot.JobSearch.common.enums.ScaffoldType;
import com.Grassroot.JobSearch.common.enums.TaskSessionStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "task_sessions")
public class TaskSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "job_id")
    private String jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "scaffold_type")
    private ScaffoldType scaffoldType;

    @Column(name = "current_step")
    private int currentStep = 1;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "steps_data")
    private List<Map<String, Object>> stepsData = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TaskSessionStatus status = TaskSessionStatus.in_progress;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @PrePersist
    void prePersist() { startedAt = Instant.now(); }

    public String getId() { return id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    public ScaffoldType getScaffoldType() { return scaffoldType; }
    public void setScaffoldType(ScaffoldType scaffoldType) { this.scaffoldType = scaffoldType; }
    public int getCurrentStep() { return currentStep; }
    public void setCurrentStep(int currentStep) { this.currentStep = currentStep; }
    public List<Map<String, Object>> getStepsData() { return stepsData; }
    public void setStepsData(List<Map<String, Object>> stepsData) { this.stepsData = stepsData; }
    public TaskSessionStatus getStatus() { return status; }
    public void setStatus(TaskSessionStatus status) { this.status = status; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
