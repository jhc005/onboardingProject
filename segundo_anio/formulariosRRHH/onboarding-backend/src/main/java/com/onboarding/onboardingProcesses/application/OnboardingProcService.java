package com.onboarding.onboardingProcesses.application;

import com.onboarding.accessTokens.domain.model.AcessTokens;
import com.onboarding.accessTokens.domain.outport.AccessTokensRepositoryPort;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.candidates.application.CandidatesService;
import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.notifications.application.NotificationsService;
import com.onboarding.onboardingProcesses.application.dto.OnboardingProcDto;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.onboardingProcesses.domain.valueobject.OnboardingStatus;
import com.onboarding.onboardingProcesses.mapper.OnboardingProcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnboardingProcService {

    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;
    private final AdminUsersRepositoryPort adminUsersPort;
    private final AccessTokensRepositoryPort tokenPort;
    private final NotificationsService notificationsService;
    private final OnboardingProcMapper mapper;

    public List<OnboardingProcDto.Response> findAll() {
        return processPort.findAll().stream().map(mapper::toResponse).toList();
    }

    public OnboardingProcDto.Response findById(String id) {
        OnboardingProc process = processPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));
        return mapper.toResponse(process);
    }

    @Transactional
    public OnboardingProcDto.Response submit(String processId) {
        OnboardingProc process = processPort.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        if (process.getStatus() != OnboardingStatus.IN_PROGRESS) {
            throw new IllegalStateException("El proceso no está en estado IN_PROGRESS.");
        }

        process.setStatus(OnboardingStatus.SUBMITTED);
        process.setSubmittedAt(LocalDateTime.now());
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();
        notificationsService.sendFormSubmitted(processId, fullName);

        return mapper.toResponse(process);
    }

    @Transactional
    public OnboardingProcDto.Response validate(String processId, String adminEmail) {
        Long adminId = adminUsersPort.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado."))
                .getId();

        OnboardingProc process = processPort.findById(processId)
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
        notificationsService.sendValidated(processId, candidate.getEmail(), fullName);

        return mapper.toResponse(process);
    }

    @Transactional
    public void resendAccessLink(String processId) {
        OnboardingProc process = processPort.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();

        String rawToken = UUID.randomUUID().toString();
        AcessTokens newToken = new AcessTokens();
        newToken.setOnboardingProcessId(processId);
        newToken.setTokenHash(CandidatesService.hashToken(rawToken));
        newToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        newToken.setIsUsed(false);
        newToken.setAttempts(0);
        tokenPort.save(newToken);

        notificationsService.sendAccessLink(processId, candidate, rawToken);
    }

    @Transactional
    public OnboardingProcDto.Response reject(String processId, String adminEmail, String notes) {
        Long adminId = adminUsersPort.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado."))
                .getId();

        OnboardingProc process = processPort.findById(processId)
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        process.setStatus(OnboardingStatus.REJECTED);
        process.setValidatedAt(LocalDateTime.now());
        process.setValidatedById(adminId);
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();

        String rawToken = UUID.randomUUID().toString();
        AcessTokens newToken = new AcessTokens();
        newToken.setOnboardingProcessId(processId);
        newToken.setTokenHash(CandidatesService.hashToken(rawToken));
        newToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        newToken.setIsUsed(false);
        newToken.setAttempts(0);
        tokenPort.save(newToken);

        notificationsService.sendRejected(processId, candidate.getEmail(), fullName, notes, rawToken);

        return mapper.toResponse(process);
    }
}
