package com.Grassroot.JobSearch.task;

import com.Grassroot.JobSearch.common.enums.ScaffoldType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Map;

@Entity
@Table(name = "task_templates", uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "scaffold_type"}))
public class TaskTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "job_id")
    private String jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "scaffold_type")
    private ScaffoldType scaffoldType;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> content;

    public String getId() { return id; }
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    public ScaffoldType getScaffoldType() { return scaffoldType; }
    public void setScaffoldType(ScaffoldType scaffoldType) { this.scaffoldType = scaffoldType; }
    public Map<String, Object> getContent() { return content; }
    public void setContent(Map<String, Object> content) { this.content = content; }
}
