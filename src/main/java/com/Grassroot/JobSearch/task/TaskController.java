package com.Grassroot.JobSearch.task;

import com.Grassroot.JobSearch.config.AppConstants;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Map<String, Object> create(
            @RequestHeader(AppConstants.HEADER_SESSION_ID) String sessionId,
            @Valid @RequestBody CreateTaskRequest req) {
        return taskService.create(sessionId, req);
    }

    @GetMapping("/{id}")
    public Map<String, Object> get(@PathVariable String id) {
        return taskService.get(id);
    }

    @PostMapping("/{id}/step")
    public Map<String, Object> step(@PathVariable String id, @RequestBody StepSubmitRequest req) {
        return taskService.submitStep(id, req);
    }

    @PostMapping("/{id}/help")
    public Map<String, Object> help(@PathVariable String id, @RequestBody Map<String, String> body) {
        return taskService.help(id, body.getOrDefault("helpType", "thinking_prompt"));
    }

    @PostMapping("/{id}/feedback")
    public Map<String, Object> feedback(@PathVariable String id, @Valid @RequestBody TaskFeedbackRequest req) {
        return taskService.feedback(id, req);
    }
}
