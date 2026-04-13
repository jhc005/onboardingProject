package com.onboarding.onboardingProcesses.application.queries;

import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.onboardingProcesses.mapper.OnboardingProcMapper;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetProcessByIdQueryHandler implements QueryHandler<GetProcessByIdQuery, OnboardingProcDto.Response> {

    private final OnboardingProcRepositoryPort processPort;
    private final OnboardingProcMapper mapper;

    @Override
    public OnboardingProcDto.Response handle(GetProcessByIdQuery query) {
        return processPort.findById(query.getId())
                .map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));
    }
}
