package com.onboarding.onboardingProcesses.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OnboardingProcJpaRepository extends JpaRepository<OnboardingProcJpaEntity, String> {

    Optional<OnboardingProcJpaEntity> findByCandidate_Id(String candidateId);
    List<OnboardingProcJpaEntity> findByStatus(OnboardingProcJpaEntity.OnboardingStatus status);
}
