package com.Grassroot.JobSearch.task;

import jakarta.validation.constraints.NotBlank;

public record TaskFeedbackRequest(
        @NotBlank String likeLevel,
        @NotBlank String longTermWillingness,
        String feelingSource,
        String freeText
) {
}
