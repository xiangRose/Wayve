package com.Grassroot.JobSearch.job;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "job_models")
public class JobModel {

    @Id
    private String jobId;
    private String name;
    @Lob private String definition;
    @Lob private String coreWorkObject;
    @Lob private String typicalWorkSnippet;
    @Lob private String whyExperience;
    private int estimatedMinutes;
    private String taskStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> competencyRequirements;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> specificCompetencies;

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDefinition() { return definition; }
    public void setDefinition(String definition) { this.definition = definition; }
    public String getCoreWorkObject() { return coreWorkObject; }
    public void setCoreWorkObject(String coreWorkObject) { this.coreWorkObject = coreWorkObject; }
    public String getTypicalWorkSnippet() { return typicalWorkSnippet; }
    public void setTypicalWorkSnippet(String typicalWorkSnippet) { this.typicalWorkSnippet = typicalWorkSnippet; }
    public String getWhyExperience() { return whyExperience; }
    public void setWhyExperience(String whyExperience) { this.whyExperience = whyExperience; }
    public int getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(int estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
    public String getTaskStatus() { return taskStatus; }
    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }
    public Map<String, String> getCompetencyRequirements() { return competencyRequirements; }
    public void setCompetencyRequirements(Map<String, String> competencyRequirements) { this.competencyRequirements = competencyRequirements; }
    public List<String> getSpecificCompetencies() { return specificCompetencies; }
    public void setSpecificCompetencies(List<String> specificCompetencies) { this.specificCompetencies = specificCompetencies; }
}
