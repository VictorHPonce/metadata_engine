package com.devexup.fabric.application.usecase;

import com.devexup.fabric.domain.model.NodeDefinition;
import com.devexup.fabric.domain.repository.NodeDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDefinitionUseCase {
    private final NodeDefinitionRepository repository;

    public NodeDefinition execute(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Definición no encontrada: " + name));
    }
}