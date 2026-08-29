package com.Grassroot.JobSearch.scene;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SceneEvidenceRepository extends JpaRepository<SceneEvidence, String> {

    List<SceneEvidence> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    List<SceneEvidence> findBySessionIdAndSceneIdOrderByCreatedAtDesc(String sessionId, String sceneId);
}
