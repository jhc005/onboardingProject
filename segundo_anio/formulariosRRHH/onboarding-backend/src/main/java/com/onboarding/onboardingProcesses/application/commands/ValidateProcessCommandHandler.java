package com.onboarding.onboardingProcesses.application.commands;

import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
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
public class ValidateProcessCommandHandler implements CommandHandler<ValidateProcessCommand, OnboardingProcDto.Response> {

    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;
    private final AdminUsersRepositoryPort adminUsersPort;
    private final NotificationsService notificationsService;
    private final OnboardingProcMapper mapper;

    @Override
    @Transactional
    public OnboardingProcDto.Response handle(ValidateProcessCommand command) {
        Long adminId = adminUsersPort.findByEmail(command.getAdminEmail())
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado."))
                .getId();

        OnboardingProc process = processPort.findById(command.getProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        if (process.getStatus() != OnboardingStatus.SUBMITTED) {
            throw new IllegalStateException("El proceso no está en estado SUBMITTED.");
        }

        process.setStatus(OnboardingStatus.VALIDATED);
        process.setValidatedAt(LocalDateTime.now());
        process.setValidatedById(adminId);
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();
        notificationsService.sendValidated(command.getProcessId(), candidate.getEmail(), fullName);

        return mapper.toResponse(process);
    }
}
