package com.devexup.fabric.domain.repository;

import java.util.List;
import java.util.UUID;

import com.devexup.fabric.domain.model.NodeEdge;

public interface NodeEdgeRepository {
    NodeEdge save(NodeEdge edge);
    List<NodeEdge> findBySourceId(UUID sourceId);
    List<NodeEdge> findByTargetId(UUID targetId);
    void delete(UUID id);
}
