package com.onboarding.adminUsers.infrastructure.output.adapter;

import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.adminUsers.infrastructure.output.jpa.AdminUsersJpaRepository;
import com.onboarding.adminUsers.mapper.AdminUsersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminUsersAdapter implements AdminUsersRepositoryPort {

    private final AdminUsersJpaRepository repository;
    private final AdminUsersMapper mapper;

    @Override
    public AdminUsers save(AdminUsers adminUser) {
        return mapper.toDomain(repository.save(mapper.toEntity(adminUser)));
    }

    @Override
    public Optional<AdminUsers> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<AdminUsers> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<AdminUsers> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
