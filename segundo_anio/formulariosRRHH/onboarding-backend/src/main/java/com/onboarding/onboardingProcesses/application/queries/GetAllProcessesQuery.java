package com.onboarding.onboardingProcesses.application.queries;

import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.shared.cqrs.Query;

import java.util.List;

public class GetAllProcessesQuery implements Query<List<OnboardingProcDto.Response>> {
}
