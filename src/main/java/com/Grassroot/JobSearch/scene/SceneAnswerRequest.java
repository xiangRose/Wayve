package com.Grassroot.JobSearch.scene;

import jakarta.validation.constraints.NotBlank;

public record SceneAnswerRequest(
        @NotBlank String roleId,
        @NotBlank String answerType,
        String selectedOptionId,
        String rawAnswer
) {
}
