package com.onboarding.documentRequirements.application.queries;

import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CanModifyDocumentRequirementsQueryHandler implements QueryHandler<CanModifyDocumentRequirementsQuery, Boolean> {

    private final OnboardingProcRepositoryPort onboardingProcPort;

    @Override
    public Boolean handle(CanModifyDocumentRequirementsQuery query) {
        return !onboardingProcPort.existsActiveProcesses();
    }
}
