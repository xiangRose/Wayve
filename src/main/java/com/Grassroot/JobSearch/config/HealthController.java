package com.Grassroot.JobSearch.config;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "ok", "time", java.time.Instant.now().toString());
    }

    @PostMapping("/analytics/events")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, Object> analytics(@RequestBody Map<String, Object> body) {
        return Map.of("ok", true);
    }
}
