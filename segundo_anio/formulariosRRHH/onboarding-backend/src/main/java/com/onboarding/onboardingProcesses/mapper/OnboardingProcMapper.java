package com.onboarding.onboardingProcesses.mapper;

import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnboardingProcMapper {

    private final CandidatesRepositoryPort candidatesPort;

    public OnboardingProc toDomain(OnboardingProcJpaEntity entity) {
        OnboardingProc domain = new OnboardingProc();
        domain.setId(entity.getId());
        domain.setCandidateId(entity.getCandidate() != null ? entity.getCandidate().getId() : null);
        domain.setStatus(entity.getStatus() != null
                ? OnboardingProc.OnboardingStatus.valueOf(entity.getStatus().name()) : null);
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setSubmittedAt(entity.getSubmittedAt());
        domain.setValidatedAt(entity.getValidatedAt());
        domain.setValidatedById(entity.getValidatedBy() != null ? entity.getValidatedBy().getId() : null);
        return domain;
    }

    public OnboardingProcJpaEntity toEntity(OnboardingProc domain) {
        OnboardingProcJpaEntity entity = new OnboardingProcJpaEntity();
        entity.setId(domain.getId());
        entity.setStatus(domain.getStatus() != null
                ? OnboardingProcJpaEntity.OnboardingStatus.valueOf(domain.getStatus().name()) : null);
        entity.setSubmittedAt(domain.getSubmittedAt());
        entity.setValidatedAt(domain.getValidatedAt());
        return entity;
    }

    public OnboardingProcDto.Response toResponse(OnboardingProc process) {
        Candidates candidate = process.getCandidateId() != null
                ? candidatesPort.findById(process.getCandidateId()).orElse(null) : null;

        return OnboardingProcDto.Response.builder()
                .id(process.getId())
                .candidateId(process.getCandidateId())
                .candidateEmail(candidate != null ? candidate.getEmail() : null)
                .candidateFirstName(candidate != null ? candidate.getFirstName() : null)
                .candidateLastName(candidate != null ? candidate.getLastName() : null)
                .status(process.getStatus() != null ? process.getStatus().name() : null)
                .createdAt(process.getCreatedAt())
                .submittedAt(process.getSubmittedAt())
                .validatedAt(process.getValidatedAt())
                .build();
    }
}
