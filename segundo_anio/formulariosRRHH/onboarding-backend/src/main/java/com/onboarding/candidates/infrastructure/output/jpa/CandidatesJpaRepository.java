package com.onboarding.candidates.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CandidatesJpaRepository extends JpaRepository<CandidatesJpaEntity, String> {

    boolean existsByEmail(String email);
}
