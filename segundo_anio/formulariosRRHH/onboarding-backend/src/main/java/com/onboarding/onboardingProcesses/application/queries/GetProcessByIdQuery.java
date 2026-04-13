package com.onboarding.onboardingProcesses.application.queries;

import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.shared.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetProcessByIdQuery implements Query<OnboardingProcDto.Response> {
    private final String id;
}
