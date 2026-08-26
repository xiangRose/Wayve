package com.Grassroot.JobSearch.task;

import com.Grassroot.JobSearch.common.ApiException;
import com.Grassroot.JobSearch.common.enums.ScaffoldType;
import com.Grassroot.JobSearch.common.enums.TaskSessionStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private static final int TOTAL = 6;

    private final TaskSessionRepository sessionRepository;
    private final TaskTemplateRepository templateRepository;
    private final InterestSignalRepository interestSignalRepository;

    public TaskService(
            TaskSessionRepository sessionRepository,
            TaskTemplateRepository templateRepository,
            InterestSignalRepository interestSignalRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.templateRepository = templateRepository;
        this.interestSignalRepository = interestSignalRepository;
    }

    @Transactional
    public Map<String, Object> create(String sessionId, CreateTaskRequest req) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "MISSING_SESSION", "缺少 X-Session-Id");
        }
        ScaffoldType scaffold = ScaffoldType.valueOf(req.scaffoldType());
        TaskTemplate template = templateRepository.findByJobIdAndScaffoldType(req.jobId(), scaffold)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "TEMPLATE_NOT_FOUND", "未找到任务模板"));

        TaskSession ts = new TaskSession();
        ts.setSessionId(sessionId);
        ts.setJobId(req.jobId());
        ts.setScaffoldType(scaffold);
        ts = sessionRepository.save(ts);
        return response(ts, template.getContent(), 1);
    }

    public Map<String, Object> get(String taskSessionId) {
        TaskSession ts = load(taskSessionId);
        return response(ts, templateOf(ts).getContent(), ts.getCurrentStep());
    }

    @Transactional
    public Map<String, Object> submitStep(String taskSessionId, StepSubmitRequest req) {
        TaskSession ts = load(taskSessionId);
        TaskTemplate template = templateOf(ts);

        List<Map<String, Object>> steps = new ArrayList<>(ts.getStepsData());
        Map<String, Object> row = new HashMap<>();
        row.put("step", ts.getCurrentStep());
        row.put("answer", req.answer());
        row.put("events", req.events() == null ? List.of() : req.events());
        row.put("submittedAt", Instant.now().toString());
        steps.add(row);

        int next = ts.getCurrentStep() + 1;
        boolean done = next > TOTAL;
        ts.setStepsData(steps);
        ts.setCurrentStep(done ? TOTAL : next);
        if (done) {
            ts.setStatus(TaskSessionStatus.completed);
            ts.setCompletedAt(Instant.now());
        }
        sessionRepository.save(ts);

        Map<String, Object> res = response(ts, template.getContent(), done ? TOTAL : next);
        if (done) {
            res.put("message", "任务已完成，请前往兴趣反馈");
        }
        return res;
    }

    public Map<String, Object> help(String taskSessionId, String helpType) {
        TaskSession ts = load(taskSessionId);
        Map<String, Object> step = step(templateOf(ts).getContent(), ts.getCurrentStep());
        String content = "可以先明确：当前最大的障碍是什么？判断依据是什么？";
        if (step.get("help") instanceof Map<?, ?> help) {
            Object prompts = help.get("thinkingPrompts");
            if (prompts instanceof List<?> list && !list.isEmpty()) {
                content = String.valueOf(list.get(0));
            }
        }
        return Map.of("content", content, "helpType", helpType);
    }

    @Transactional
    public Map<String, Object> feedback(String taskSessionId, TaskFeedbackRequest req) {
        InterestSignal signal = new InterestSignal();
        signal.setTaskSessionId(taskSessionId);
        signal.setLikeLevel(req.likeLevel());
        signal.setLongTermWillingness(req.longTermWillingness());
        signal.setFeelingSource(req.feelingSource());
        signal.setFreeText(req.freeText());
        interestSignalRepository.save(signal);
        return Map.of("ok", true);
    }

    private TaskSession load(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "TASK_NOT_FOUND", "任务不存在"));
    }

    private TaskTemplate templateOf(TaskSession ts) {
        return templateRepository.findByJobIdAndScaffoldType(ts.getJobId(), ts.getScaffoldType())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "TEMPLATE_NOT_FOUND", "模板不存在"));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> step(Map<String, Object> content, int stepNum) {
        List<Map<String, Object>> steps = (List<Map<String, Object>>) content.get("steps");
        return steps.get(stepNum - 1);
    }

    private Map<String, Object> response(TaskSession ts, Map<String, Object> content, int stepNum) {
        Map<String, Object> step = step(content, stepNum);
        Map<String, Object> stepContent = Map.of(
                "step", stepNum,
                "totalSteps", TOTAL,
                "stepTitle", step.get("stepTitle"),
                "context", step.get("context"),
                "decision", step.get("decision"),
                "help", step.get("help"));

        Map<String, Object> res = new HashMap<>();
        res.put("taskSessionId", ts.getId());
        res.put("jobId", ts.getJobId());
        res.put("scaffoldType", ts.getScaffoldType().name());
        res.put("currentStep", stepNum);
        res.put("status", ts.getStatus().name());
        res.put("stepContent", stepContent);
        return res;
    }
}
