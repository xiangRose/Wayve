package com.Grassroot.JobSearch.session;

import jakarta.validation.constraints.NotBlank;

public record ProfileRequest(
        @NotBlank String userStage,
        @NotBlank String clarityLevel,
        @NotBlank String currentStatus,
        String education,
        String backgroundText,
        String teamRoleDescription,
        String workPreference,
        String resumeText
) {
}
