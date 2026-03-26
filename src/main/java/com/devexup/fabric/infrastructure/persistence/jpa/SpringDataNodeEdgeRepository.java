package com.devexup.fabric.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SpringDataNodeEdgeRepository extends JpaRepository<NodeEdgeEntity, UUID> {
    List<NodeEdgeEntity> findBySourceId(UUID sourceId);
    List<NodeEdgeEntity> findByTargetId(UUID targetId);
}