package com.onboarding.onboardingProcesses.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onboarding.onboardingProcesses.domain.valueobject.OnboardingStatus;

import java.util.List;

public interface OnboardingProcJpaRepository extends JpaRepository<OnboardingProcJpaEntity, String> {

    boolean existsByStatusIn(List<OnboardingStatus> statuses);
}
