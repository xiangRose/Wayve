package com.Grassroot.JobSearch.task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "interest_signals")
public class InterestSignal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String taskSessionId;
    private String likeLevel;
    private String longTermWillingness;
    private String feelingSource;
    private String freeText;
    private Instant createdAt;

    @PrePersist
    void prePersist() { createdAt = Instant.now(); }

    public void setTaskSessionId(String taskSessionId) { this.taskSessionId = taskSessionId; }
    public void setLikeLevel(String likeLevel) { this.likeLevel = likeLevel; }
    public void setLongTermWillingness(String longTermWillingness) { this.longTermWillingness = longTermWillingness; }
    public void setFeelingSource(String feelingSource) { this.feelingSource = feelingSource; }
    public void setFreeText(String freeText) { this.freeText = freeText; }
    public String getTaskSessionId() { return taskSessionId; }
    public String getLikeLevel() { return likeLevel; }
    public String getLongTermWillingness() { return longTermWillingness; }
    public String getFeelingSource() { return feelingSource; }
    public String getFreeText() { return freeText; }
}
