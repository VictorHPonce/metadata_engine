package com.devexup.fabric.domain.repository;

import com.devexup.fabric.domain.model.NodeDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NodeDefinitionRepository {
    NodeDefinition save(NodeDefinition definition);
    Optional<NodeDefinition> findByName(String name);
    List<NodeDefinition> findAll();
    Optional<NodeDefinition> findById(UUID id);
}