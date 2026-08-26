package com.Grassroot.JobSearch.report;

import jakarta.validation.constraints.NotBlank;

public record TargetSelectRequest(@NotBlank String targetJobId) {
}
