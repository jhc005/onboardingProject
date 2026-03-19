package com.onboarding.onboardingProcesses.domain.outport;

import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc.OnboardingStatus;

import java.util.List;
import java.util.Optional;

public interface OnboardingProcRepositoryPort {

    OnboardingProc save(OnboardingProc process);
    Optional<OnboardingProc> findById(String id);
    Optional<OnboardingProc> findByCandidateId(String candidateId);
    List<OnboardingProc> findAll();
    List<OnboardingProc> findByStatus(OnboardingStatus status);
    void deleteById(String id);
}
