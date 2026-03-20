package com.onboarding.onboardingProcesses.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.onboarding.onboardingProcesses.domain.valueobject.OnboardingStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingProc {

    private String id;
    private String candidateId;
    private OnboardingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime validatedAt;
    private Long validatedById;

}
