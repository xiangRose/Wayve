package com.Grassroot.JobSearch.report;

import com.Grassroot.JobSearch.ai.DemoDataService;
import com.Grassroot.JobSearch.common.ApiException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final DemoDataService demoDataService;

    public ReportService(ReportRepository reportRepository, DemoDataService demoDataService) {
        this.reportRepository = reportRepository;
        this.demoDataService = demoDataService;
    }

    @Transactional
    public Map<String, Object> generate(String sessionId, boolean demoMode) {
        if (demoMode) {
            return demoResponse(sessionId);
        }
        return reportRepository.findFirstBySessionIdOrderByGeneratedAtDesc(sessionId)
                .map(this::toMap)
                .orElseGet(() -> toMap(createFromDemo(sessionId)));
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

    private ExplorationReport createFromDemo(String sessionId) {
        Map<String, Object> demo = demoDataService.report();
        ExplorationReport r = new ExplorationReport();
        r.setSessionId(sessionId);
        r.setResumeRadarData(castMap(demo.get("resumeRadar")));
        r.setTaskEvidenceSummary(castMap(demo.get("taskEvidenceByJob")));
        r.setInterestSignals(demoDataService.interestSignals());
        r.setGapAnalysis(castMap(demo.get("gapAnalysis")));
        r.setActionTasks(castList(demo.get("actionTasks")));
        r.setComparisonSummary((String) demo.get("comparisonSummary"));
        r.setBoundaryNotice((String) demo.get("boundaryNotice"));
        r.setSelectedTargetJob(demoDataService.selectedTargetJob());
        return reportRepository.save(r);
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

    @SuppressWarnings("unchecked")
    private Map<String, Object> castMap(Object v) { return (Map<String, Object>) v; }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castList(Object v) { return (List<Map<String, Object>>) v; }
}
