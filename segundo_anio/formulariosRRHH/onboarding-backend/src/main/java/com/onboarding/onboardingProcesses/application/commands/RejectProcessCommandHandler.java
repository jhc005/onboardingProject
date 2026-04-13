package com.onboarding.onboardingProcesses.application.commands;

import com.onboarding.accessTokens.domain.model.AcessTokens;
import com.onboarding.accessTokens.domain.outport.AccessTokensRepositoryPort;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RejectProcessCommandHandler implements CommandHandler<RejectProcessCommand, OnboardingProcDto.Response> {

    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;
    private final AdminUsersRepositoryPort adminUsersPort;
    private final AccessTokensRepositoryPort tokenPort;
    private final NotificationsService notificationsService;
    private final OnboardingProcMapper mapper;

    @Override
    @Transactional
    public OnboardingProcDto.Response handle(RejectProcessCommand command) {
        Long adminId = adminUsersPort.findByEmail(command.getAdminEmail())
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado."))
                .getId();

        OnboardingProc process = processPort.findById(command.getProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        process.setStatus(OnboardingStatus.REJECTED);
        process.setValidatedAt(LocalDateTime.now());
        process.setValidatedById(adminId);
        processPort.save(process);

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();
        String fullName = candidate.getFirstName() + " " + candidate.getLastName();

        String rawToken = UUID.randomUUID().toString();
        AcessTokens newToken = new AcessTokens();
        newToken.setOnboardingProcessId(command.getProcessId());
        newToken.setTokenHash(hashToken(rawToken));
        newToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        newToken.setIsUsed(false);
        newToken.setAttempts(0);
        tokenPort.save(newToken);

        notificationsService.sendRejected(command.getProcessId(), candidate.getEmail(), fullName, command.getNotes(), rawToken);

        return mapper.toResponse(process);
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
}
