package com.Grassroot.JobSearch.common;

public final class JobIdMapper {

    private JobIdMapper() {}

    public static String toBackend(String jobId) {
        if (jobId == null) {
            return "";
        }
        return switch (jobId) {
            case "ai_product" -> "ai_pm";
            case "ai_ui_design" -> "ai_ux";
            case "ai_ops" -> "ai_operator";
            case "ai_data_eval" -> "ai_researcher";
            case "ai_app_dev" -> "ai_consultant";
            default -> jobId;
        };
    }

    public static String toFrontend(String jobId) {
        if (jobId == null) {
            return "";
        }
        return switch (jobId) {
            case "ai_pm" -> "ai_product";
            case "ai_ux" -> "ai_ui_design";
            case "ai_operator" -> "ai_ops";
            case "ai_researcher" -> "ai_data_eval";
            case "ai_consultant" -> "ai_app_dev";
            default -> jobId;
        };
    }
}
