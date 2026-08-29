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

    private static final List<String> TEMPLATE_PATHS = List.of(
            "seed/task-templates/ai_product.career_changer.json",
            "seed/task-templates/ai_ui_design.career_changer.json",
            "seed/task-templates/ai_ops.career_changer.json",
            "seed/task-templates/ai_data_eval.career_changer.json",
            "seed/task-templates/ai_app_dev.career_changer.json");

    private final JsonResourceLoader json;
    private final JobRepository jobRepository;
    private final TaskTemplateRepository taskTemplateRepository;

    public SeedDataLoader(JsonResourceLoader json, JobRepository jobRepository, TaskTemplateRepository taskTemplateRepository) {
        this.json = json;
        this.jobRepository = jobRepository;
        this.taskTemplateRepository = taskTemplateRepository;
    }

    @Override
    public void run(String... args) {
        if (jobRepository.count() == 0) {
            seedJobs();
        } else {
            ensureAllJobsInteractive();
        }
        TEMPLATE_PATHS.forEach(this::seedTemplateIfMissing);
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

    private void seedTemplateIfMissing(String path) {
        Map<String, Object> data = json.load(path, new TypeReference<>() {});
        String jobId = (String) data.get("jobId");
        ScaffoldType scaffold = ScaffoldType.valueOf((String) data.get("scaffoldType"));
        if (taskTemplateRepository.findByJobIdAndScaffoldType(jobId, scaffold).isPresent()) {
            return;
        }
        seedTemplate(path);
    }

    private void ensureAllJobsInteractive() {
        jobRepository.findAll().forEach(job -> {
            if (!"interactive".equals(job.getTaskStatus())) {
                job.setTaskStatus("interactive");
                jobRepository.save(job);
            }
        });
    }

    private void seedTemplate(String path) {
        Map<String, Object> data = json.load(path, new TypeReference<>() {});
        TaskTemplate t = new TaskTemplate();
        t.setJobId((String) data.get("jobId"));
        t.setScaffoldType(ScaffoldType.valueOf((String) data.get("scaffoldType")));
        t.setContent(data);
        taskTemplateRepository.save(t);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> castMap(Object v) { return (Map<String, String>) v; }

    @SuppressWarnings("unchecked")
    private List<String> castList(Object v) { return v == null ? List.of() : (List<String>) v; }
}
