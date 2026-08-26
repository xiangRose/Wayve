package com.Grassroot.JobSearch.report;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<ExplorationReport, String> {
    Optional<ExplorationReport> findFirstBySessionIdOrderByGeneratedAtDesc(String sessionId);
}
