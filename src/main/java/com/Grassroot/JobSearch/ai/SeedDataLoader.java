package com.Grassroot.JobSearch.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.Grassroot.JobSearch.common.JsonResourceLoader;
import com.Grassroot.JobSearch.common.enums.ScaffoldType;
import com.Grassroot.JobSearch.job.JobModel;
import com.Grassroot.JobSearch.job.JobRepository;
import com.Grassroot.JobSearch.task.TaskTemplate;
import com.Grassroot.JobSearch.task.TaskTemplateRepository;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeedDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedDataLoader.class);

    private final JsonResourceLoader json;
    private final JobRepository jobRepository;
    private final TaskTemplateRepository taskTemplateRepository;

    public SeedDataLoader(JsonResourceLoader json, JobRepository jobRepository, TaskTemplateRepository taskTemplateRepository) {
        this.json = json;
        this.jobRepository = jobRepository;
        this.taskTemplateRepository = taskTemplateRepository;
    }

    private static final List<String> TASK_TEMPLATE_PATHS = List.of(
            "seed/task-templates/ai_pm.career_changer.json",
            "seed/task-templates/ai_ux.career_changer.json",
            "seed/task-templates/ai_operator.career_changer.json",
            "seed/task-templates/ai_researcher.career_changer.json",
            "seed/task-templates/ai_consultant.career_changer.json"
    );

    @Override
    public void run(String... args) {
        if (jobRepository.count() == 0) {
            seedJobs();
        }
        upsertTaskTemplates();
        log.info("Seed 完成");
    }

    private void seedJobs() {
        Map<String, List<Map<String, Object>>> root = json.load("seed/jobs.json", new TypeReference<>() {});
        for (Map<String, Object> j : root.get("jobs")) {
            JobModel m = new JobModel();
            m.setJobId((String) j.get("jobId"));
            m.setName((String) j.get("name"));
            m.setDefinition((String) j.get("definition"));
            m.setCoreWorkObject((String) j.get("coreWorkObject"));
            m.setTypicalWorkSnippet((String) j.get("typicalWorkSnippet"));
            m.setWhyExperience((String) j.get("whyExperience"));
            m.setEstimatedMinutes((Integer) j.get("estimatedMinutes"));
            m.setTaskStatus((String) j.get("taskStatus"));
            m.setCompetencyRequirements(castMap(j.get("competencyRequirements")));
            m.setSpecificCompetencies(castList(j.get("specificCompetencies")));
            jobRepository.save(m);
        }
    }

    private void upsertTaskTemplates() {
        for (String path : TASK_TEMPLATE_PATHS) {
            upsertTemplate(path);
        }
    }

    private void upsertTemplate(String path) {
        Map<String, Object> data = json.load(path, new TypeReference<>() {});
        String jobId = (String) data.get("jobId");
        ScaffoldType scaffold = ScaffoldType.valueOf((String) data.get("scaffoldType"));
        TaskTemplate t = taskTemplateRepository.findByJobIdAndScaffoldType(jobId, scaffold)
                .orElseGet(TaskTemplate::new);
        t.setJobId(jobId);
        t.setScaffoldType(scaffold);
        t.setContent(data);
        taskTemplateRepository.save(t);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> castMap(Object v) { return (Map<String, String>) v; }

    @SuppressWarnings("unchecked")
    private List<String> castList(Object v) { return v == null ? List.of() : (List<String>) v; }
}
