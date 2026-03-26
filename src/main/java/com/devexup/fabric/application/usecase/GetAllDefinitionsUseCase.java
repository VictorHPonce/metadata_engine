package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.NodeDefinition;
import com.devexup.fabric.domain.repository.NodeDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllDefinitionsUseCase {
    private final NodeDefinitionRepository repository;

    public List<NodeDefinition> execute() {
        return repository.findAll();
    }
}