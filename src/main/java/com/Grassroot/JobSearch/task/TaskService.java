package com.Grassroot.JobSearch.task;

import com.Grassroot.JobSearch.common.ApiException;
import com.Grassroot.JobSearch.common.JobIdMapper;
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
    private final MicrotaskBankService microtaskBank;

    public TaskService(
            TaskSessionRepository sessionRepository,
            TaskTemplateRepository templateRepository,
            InterestSignalRepository interestSignalRepository,
            MicrotaskBankService microtaskBank
    ) {
        this.sessionRepository = sessionRepository;
        this.templateRepository = templateRepository;
        this.interestSignalRepository = interestSignalRepository;
        this.microtaskBank = microtaskBank;
    }

    @Transactional
    public Map<String, Object> create(String sessionId, CreateTaskRequest req) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "MISSING_SESSION", "缺少 X-Session-Id");
        }
        ScaffoldType scaffold = ScaffoldType.valueOf(req.scaffoldType());
        String backendJobId = JobIdMapper.toBackend(req.jobId());
        ensureTemplateExists(backendJobId, scaffold);

        List<Map<String, Object>> plan = microtaskBank.buildMixedQuestionPlan(backendJobId);
        Map<String, Object> content = microtaskBank.buildTemplateFromPlan(backendJobId, plan);

        TaskSession ts = new TaskSession();
        ts.setSessionId(sessionId);
        ts.setJobId(backendJobId);
        ts.setScaffoldType(scaffold);
        Map<String, Object> meta = new HashMap<>();
        meta.put("setId", "MIXED");
        meta.put("theme", content.get("title"));
        meta.put("questionPlan", plan);
        ts.setTaskMeta(meta);
        ts = sessionRepository.save(ts);
        return response(ts, content, 1);
    }

    public Map<String, Object> get(String taskSessionId) {
        TaskSession ts = load(taskSessionId);
        return response(ts, contentOf(ts), effectiveStep(ts));
    }

    @Transactional
    public Map<String, Object> submitStep(String taskSessionId, StepSubmitRequest req) {
        TaskSession ts = load(taskSessionId);
        Map<String, Object> content = contentOf(ts);
        Map<String, Object> stepDef = step(content, ts.getCurrentStep());

        List<Map<String, Object>> steps = new ArrayList<>(ts.getStepsData());
        Map<String, Object> row = new HashMap<>();
        row.put("step", ts.getCurrentStep());
        row.put("answer", req.answer());
        row.put("events", req.events() == null ? List.of() : req.events());
        row.put("submittedAt", Instant.now().toString());
        enrichScore(row, stepDef, req.answer());

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

        Map<String, Object> res = response(ts, content, done ? TOTAL : next);
        if (done) {
            res.put("message", "任务已完成，请前往兴趣反馈");
            res.put("taskRadar", microtaskBank.buildRadarFromSteps(steps, stepsOf(content)));
        }
        return res;
    }

    public Map<String, Object> help(String taskSessionId, String helpType) {
        return Map.of("content", "可以先明确：当前最大的障碍是什么？判断依据是什么？", "helpType", helpType);
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

    public Map<String, Object> radarForSession(String taskSessionId) {
        TaskSession ts = load(taskSessionId);
        Map<String, Object> content = contentOf(ts);
        return microtaskBank.buildRadarFromSteps(ts.getStepsData(), stepsOf(content));
    }

    private TaskSession load(String id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "TASK_NOT_FOUND", "任务不存在"));
    }

    private void ensureTemplateExists(String jobId, ScaffoldType scaffold) {
        templateRepository.findByJobIdAndScaffoldType(jobId, scaffold)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "TEMPLATE_NOT_FOUND", "未找到任务模板"));
    }

    private Map<String, Object> contentOf(TaskSession ts) {
        return microtaskBank.buildContentForSession(ts);
    }

    private String stringMeta(TaskSession ts, String key) {
        Map<String, Object> meta = ts.getTaskMeta();
        if (meta == null || meta.get(key) == null) {
            return "";
        }
        return String.valueOf(meta.get(key));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> step(Map<String, Object> content, int stepNum) {
        return stepsOf(content).get(stepNum - 1);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> stepsOf(Map<String, Object> content) {
        return (List<Map<String, Object>>) content.get("steps");
    }

    private int effectiveStep(TaskSession ts) {
        if (ts.getStatus() == TaskSessionStatus.completed) {
            return TOTAL;
        }
        return ts.getCurrentStep();
    }

  @SuppressWarnings("unchecked")
    private void enrichScore(Map<String, Object> row, Map<String, Object> stepDef, Map<String, Object> answer) {
        if (answer == null) {
            return;
        }
        String optionId = answer.get("selectedOptionId") == null ? "" : String.valueOf(answer.get("selectedOptionId"));
        int raw = microtaskBank.resolveRawScore(stepDef, optionId);
        row.put("dimension", stepDef.get("dimension"));
        row.put("rawScore", raw);
        row.put("radarScore", microtaskBank.toRadarScore(raw));
    }

    private Map<String, Object> response(TaskSession ts, Map<String, Object> content, int stepNum) {
        Map<String, Object> step = step(content, Math.min(stepNum, TOTAL));
        Map<String, Object> stepContent = buildStepContent(step, stepNum);

        Map<String, Object> res = new HashMap<>();
        res.put("taskSessionId", ts.getId());
        res.put("jobId", JobIdMapper.toFrontend(ts.getJobId()));
        res.put("scaffoldType", ts.getScaffoldType().name());
        res.put("currentStep", stepNum);
        res.put("status", ts.getStatus().name());
        res.put("setId", stringMeta(ts, "setId"));
        Map<String, Object> meta = ts.getTaskMeta();
        if (meta != null && meta.get("questionPlan") instanceof List<?> plan) {
            res.put("questionPlan", plan);
        }
        res.put("stepContent", stepContent);
        return res;
    }

    private Map<String, Object> buildStepContent(Map<String, Object> step, int stepNum) {
        if ("microtask_choice".equals(step.get("stepType"))) {
            return MicrotaskStepView.publicStep(step, stepNum, TOTAL);
        }
        Map<String, Object> legacy = new HashMap<>();
        legacy.put("step", stepNum);
        legacy.put("totalSteps", TOTAL);
        legacy.put("stepTitle", step.get("stepTitle"));
        legacy.put("context", step.get("context"));
        legacy.put("decision", step.get("decision"));
        legacy.put("help", step.get("help"));
        return legacy;
    }
}
