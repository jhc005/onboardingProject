package com.onboarding.onboardingProcesses.domain.outport;

import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;

import java.util.List;
import java.util.Optional;

public interface OnboardingProcRepositoryPort {

    OnboardingProc save(OnboardingProc process);
    Optional<OnboardingProc> findById(String id);
    List<OnboardingProc> findAll();
    boolean existsActiveProcesses();
}
