package com.Grassroot.JobSearch.task;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(@NotBlank String jobId, @NotBlank String scaffoldType) {
}
