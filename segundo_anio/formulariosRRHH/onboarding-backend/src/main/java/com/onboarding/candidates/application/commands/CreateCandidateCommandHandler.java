package com.onboarding.candidates.application.commands;

import com.onboarding.accessTokens.domain.model.AcessTokens;
import com.onboarding.accessTokens.domain.outport.AccessTokensRepositoryPort;
import com.onboarding.candidates.application.dto.CandidatesDto;
import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.candidates.mapper.CandidatesMapper;
import com.onboarding.notifications.application.NotificationsService;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.onboardingProcesses.domain.valueobject.OnboardingStatus;
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
public class CreateCandidateCommandHandler implements CommandHandler<CreateCandidateCommand, CandidatesDto.Response> {

    private final CandidatesRepositoryPort candidatesPort;
    private final OnboardingProcRepositoryPort processPort;
    private final AccessTokensRepositoryPort tokenPort;
    private final NotificationsService notificationsService;
    private final CandidatesMapper mapper;

    @Override
    @Transactional
    public CandidatesDto.Response handle(CreateCandidateCommand command) {
        if (candidatesPort.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("Ya existe un candidato con ese email.");
        }

        Candidates candidate = new Candidates();
        candidate.setEmail(command.getEmail());
        candidate.setFirstName(command.getFirstName());
        candidate.setLastName(command.getLastName());
        candidate = candidatesPort.save(candidate);

        OnboardingProc process = new OnboardingProc();
        process.setCandidateId(candidate.getId());
        process.setStatus(OnboardingStatus.PENDING_ACCESS);
        process = processPort.save(process);

        String rawToken = UUID.randomUUID().toString();
        String tokenHash = hashToken(rawToken);

        AcessTokens token = new AcessTokens();
        token.setOnboardingProcessId(process.getId());
        token.setTokenHash(tokenHash);
        token.setExpiresAt(LocalDateTime.now().plusDays(30));
        token.setIsUsed(false);
        token.setAttempts(0);
        tokenPort.save(token);

        notificationsService.sendAccessLink(process.getId(), candidate, rawToken);

        return mapper.toResponse(candidate);
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
