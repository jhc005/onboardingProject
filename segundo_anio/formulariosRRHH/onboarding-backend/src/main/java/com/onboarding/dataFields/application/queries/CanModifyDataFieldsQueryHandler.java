package com.onboarding.dataFields.application.queries;

import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CanModifyDataFieldsQueryHandler implements QueryHandler<CanModifyDataFieldsQuery, Boolean> {

    private final OnboardingProcRepositoryPort onboardingProcPort;

    @Override
    public Boolean handle(CanModifyDataFieldsQuery query) {
        return !onboardingProcPort.existsActiveProcesses();
    }
}
