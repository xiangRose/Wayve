package com.Grassroot.JobSearch.task;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestSignalRepository extends JpaRepository<InterestSignal, String> {

    List<InterestSignal> findByTaskSessionIdIn(Collection<String> taskSessionIds);
}
