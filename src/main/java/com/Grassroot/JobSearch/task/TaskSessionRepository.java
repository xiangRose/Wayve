package com.Grassroot.JobSearch.task;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskSessionRepository extends JpaRepository<TaskSession, String> {

    List<TaskSession> findBySessionIdOrderByStartedAtDesc(String sessionId);
}
