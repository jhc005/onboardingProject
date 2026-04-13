package com.onboarding.onboardingProcesses.application.commands;

import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.notifications.application.NotificationsService;
import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.onboardingProcesses.domain.valueobject.OnboardingStatus;
import com.onboarding.onboardingProcesses.mapper.OnboardingProcMapper;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SubmitProcessCommandHandler implements CommandHandler<SubmitProcessCommand, OnboardingProcDto.Response> {

    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;
    private final NotificationsService notificationsService;
    private final OnboardingProcMapper mapper;

    @Override
    @Transactional
    public OnboardingProcDto.Response handle(SubmitProcessCommand command) {
        OnboardingProc process = processPort.findById(command.getProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        if (process.getStatus() != OnboardingStatus.IN_PROGRESS) {
            throw new IllegalStateException("El proceso no está en estado IN_PROGRESS.");
        }

        process.setStatus(OnboardingStatus.SUBMITTED);
        process.setSubmittedAt(LocalDateTime.now());
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();
        notificationsService.sendFormSubmitted(command.getProcessId(), fullName);

        return mapper.toResponse(process);
    }
}
