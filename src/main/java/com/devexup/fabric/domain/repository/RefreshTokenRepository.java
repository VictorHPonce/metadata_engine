package com.devexup.fabric.domain.repository;

import com.devexup.fabric.domain.model.RefreshToken;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    void save(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(UUID userId);
}