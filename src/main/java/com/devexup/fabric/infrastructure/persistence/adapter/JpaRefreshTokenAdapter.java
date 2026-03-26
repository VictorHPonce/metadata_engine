package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.model.RefreshToken;
import com.devexup.fabric.domain.repository.RefreshTokenRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataRefreshTokenRepository;
import com.devexup.fabric.infrastructure.persistence.mapper.RefreshTokenMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaRefreshTokenAdapter implements RefreshTokenRepository {

    private final SpringDataRefreshTokenRepository jpaRepo;
    private final RefreshTokenMapper mapper; // Inyectamos el traductor

    @Override
    public void save(RefreshToken token) {
        // Convertimos a entidad y guardamos
        jpaRepo.save(mapper.toEntity(token));
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        // Buscamos, y si existe, mapeamos a dominio
        return jpaRepo.findByToken(token)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        jpaRepo.deleteByUserId(userId);
    }
}