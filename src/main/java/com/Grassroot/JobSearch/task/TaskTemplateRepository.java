package com.Grassroot.JobSearch.task;

import com.Grassroot.JobSearch.common.enums.ScaffoldType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTemplateRepository extends JpaRepository<TaskTemplate, String> {
    Optional<TaskTemplate> findByJobIdAndScaffoldType(String jobId, ScaffoldType scaffoldType);
}
