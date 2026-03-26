package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.model.NodeEdge;
import com.devexup.fabric.domain.repository.NodeEdgeRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataNodeEdgeRepository;
import com.devexup.fabric.infrastructure.persistence.mapper.NodeEdgeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaNodeEdgeRepositoryAdapter implements NodeEdgeRepository {

    private final SpringDataNodeEdgeRepository jpaRepo;
    private final NodeEdgeMapper mapper;

    @Override
    public NodeEdge save(NodeEdge edge) {
        // De dominio a entidad, guardamos, y de entidad a dominio para retornar
        return mapper.toDomain(jpaRepo.save(mapper.toEntity(edge)));
    }

    @Override
    public List<NodeEdge> findBySourceId(UUID sourceId) {
        return jpaRepo.findBySourceId(sourceId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<NodeEdge> findByTargetId(UUID targetId) {
        return jpaRepo.findByTargetId(targetId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        jpaRepo.deleteById(id);
    }
}