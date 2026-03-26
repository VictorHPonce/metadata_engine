package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.model.User;
import com.devexup.fabric.domain.repository.UserRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.SpringDataUserRepository;
import com.devexup.fabric.infrastructure.persistence.jpa.UserEntity;
import com.devexup.fabric.infrastructure.persistence.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository jpaRepo;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserEntity saved = jpaRepo.save(mapper.toEntity(user));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepo.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepo.findById(id)
                .map(mapper::toDomain);
    }
}