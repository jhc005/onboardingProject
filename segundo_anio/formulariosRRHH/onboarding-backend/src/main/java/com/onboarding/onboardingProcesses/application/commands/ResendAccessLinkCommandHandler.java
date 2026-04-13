package com.onboarding.onboardingProcesses.application.commands;

import com.onboarding.accessTokens.domain.model.AcessTokens;
import com.onboarding.accessTokens.domain.outport.AccessTokensRepositoryPort;
import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.notifications.application.NotificationsService;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
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
public class ResendAccessLinkCommandHandler implements CommandHandler<ResendAccessLinkCommand, Void> {

    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;
    private final AccessTokensRepositoryPort tokenPort;
    private final NotificationsService notificationsService;

    @Override
    @Transactional
    public Void handle(ResendAccessLinkCommand command) {
        OnboardingProc process = processPort.findById(command.getProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        Candidates candidate = candidatesPort.findById(process.getCandidateId()).orElseThrow();

        String rawToken = UUID.randomUUID().toString();
        AcessTokens newToken = new AcessTokens();
        newToken.setOnboardingProcessId(command.getProcessId());
        newToken.setTokenHash(hashToken(rawToken));
        newToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        newToken.setIsUsed(false);
        newToken.setAttempts(0);
        tokenPort.save(newToken);

        notificationsService.sendAccessLink(command.getProcessId(), candidate, rawToken);

        return null;
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
