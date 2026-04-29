package com.onboarding.accessTokens.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcessTokensJpaRepository extends JpaRepository<AcessTokensJpaEntity, Long> {

    Optional<AcessTokensJpaEntity> findByTokenHash(String tokenHash);
}
