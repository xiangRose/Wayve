package com.Grassroot.JobSearch.report;

import com.Grassroot.JobSearch.ai.AiOrchestrator;
import com.Grassroot.JobSearch.ai.DemoDataService;
import com.Grassroot.JobSearch.ai.ReportContextBuilder;
import com.Grassroot.JobSearch.common.ApiException;
import com.Grassroot.JobSearch.session.SessionService;
import com.Grassroot.JobSearch.session.UserSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private static final String DEFAULT_BOUNDARY =
            "本报告依据你主动提供的经历和本轮任务中的可观察行为生成，用于职业探索，不是招聘结论或永久能力评价。";

    private final ReportRepository reportRepository;
    private final DemoDataService demoDataService;
    private final SessionService sessionService;
    private final ReportContextBuilder reportContextBuilder;
    private final AiOrchestrator aiOrchestrator;

    public ReportService(
            ReportRepository reportRepository,
            DemoDataService demoDataService,
            SessionService sessionService,
            ReportContextBuilder reportContextBuilder,
            AiOrchestrator aiOrchestrator) {
        this.reportRepository = reportRepository;
        this.demoDataService = demoDataService;
        this.sessionService = sessionService;
        this.reportContextBuilder = reportContextBuilder;
        this.aiOrchestrator = aiOrchestrator;
    }

    @Transactional
    public Map<String, Object> generate(String sessionId, boolean demoMode) {
        if (demoMode) {
            return demoResponse(sessionId);
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "SESSION_REQUIRED", "需要 X-Session-Id");
        }
        UserSession session = sessionService.find(sessionId);
        Map<String, Object> ctx = reportContextBuilder.build(sessionId, session);
        Map<String, Object> aiResult = aiOrchestrator.generateReport(ctx);

        ExplorationReport report = new ExplorationReport();
        report.setSessionId(sessionId);
        report.setResumeRadarData(buildResumeRadar(session.getResumeEvidenceData()));
        report.setTaskEvidenceSummary(reportContextBuilder.buildTaskEvidenceByJob(sessionId));
        report.setInterestSignals(castList(ctx.get("interest_signals")));
        report.setComparisonSummary(stringVal(aiResult.get("comparisonSummary")));
        report.setGapAnalysis(castMap(aiResult.get("gapAnalysis")));
        report.setActionTasks(castList(aiResult.get("actionTasks")));
        report.setBoundaryNotice(stringVal(aiResult.getOrDefault("boundaryNotice", DEFAULT_BOUNDARY)));
        report.setSelectedTargetJob(stringVal(ctx.get("selected_target_job")));
        return toMap(reportRepository.save(report));
    }

    public Map<String, Object> get(String reportId) {
        return reportRepository.findById(reportId).map(this::toMap).orElseGet(() -> demoResponse(null));
    }

    @Transactional
    public Map<String, Object> selectTarget(String reportId, String targetJobId) {
        ExplorationReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "REPORT_NOT_FOUND", "报告不存在"));
        report.setSelectedTargetJob(targetJobId);
        return toMap(reportRepository.save(report));
    }

    private Map<String, Object> buildResumeRadar(Map<String, Object> evidence) {
        if (evidence == null || evidence.isEmpty()) {
            return Map.of("status", "empty", "message", "尚未提取履历证据，请先完善探索资料。");
        }
        return new HashMap<>(evidence);
    }

    private Map<String, Object> demoResponse(String sessionId) {
        Map<String, Object> demo = demoDataService.report();
        Map<String, Object> m = new HashMap<>();
        m.put("reportId", "demo-report");
        m.put("sessionId", sessionId);
        m.put("resumeRadar", demo.get("resumeRadar"));
        m.put("taskEvidenceByJob", demo.get("taskEvidenceByJob"));
        m.put("interestSignals", demoDataService.interestSignals());
        m.put("comparisonSummary", demo.get("comparisonSummary"));
        m.put("gapAnalysis", demo.get("gapAnalysis"));
        m.put("actionTasks", demo.get("actionTasks"));
        m.put("boundaryNotice", demo.get("boundaryNotice"));
        m.put("selectedTargetJob", demoDataService.selectedTargetJob());
        return m;
    }

    private Map<String, Object> toMap(ExplorationReport r) {
        Map<String, Object> m = new HashMap<>();
        m.put("reportId", r.getId());
        m.put("sessionId", r.getSessionId());
        m.put("resumeRadar", r.getResumeRadarData());
        m.put("taskEvidenceByJob", r.getTaskEvidenceSummary());
        m.put("interestSignals", r.getInterestSignals());
        m.put("comparisonSummary", r.getComparisonSummary());
        m.put("gapAnalysis", r.getGapAnalysis());
        m.put("actionTasks", r.getActionTasks());
        m.put("boundaryNotice", r.getBoundaryNotice());
        m.put("selectedTargetJob", r.getSelectedTargetJob());
        return m;
    }

    private static String stringVal(Object v) {
        return v == null ? "" : String.valueOf(v);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castMap(Object v) {
        return v instanceof Map<?, ?> m ? (Map<String, Object>) m : Map.of();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castList(Object v) {
        return v instanceof List<?> list ? (List<Map<String, Object>>) list : List.of();
    }
}
