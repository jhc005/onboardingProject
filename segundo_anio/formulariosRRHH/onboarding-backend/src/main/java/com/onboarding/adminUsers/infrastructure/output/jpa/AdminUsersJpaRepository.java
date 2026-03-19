package com.onboarding.adminUsers.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUsersJpaRepository extends JpaRepository<AdminUsersJpaEntity, Long> {

    Optional<AdminUsersJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
