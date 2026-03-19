package com.onboarding.candidates.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidatesJpaRepository extends JpaRepository<CandidatesJpaEntity, String> {

    Optional<CandidatesJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
