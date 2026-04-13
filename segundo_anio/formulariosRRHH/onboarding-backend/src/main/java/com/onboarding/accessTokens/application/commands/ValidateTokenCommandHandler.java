package com.onboarding.accessTokens.application.commands;

import com.onboarding.accessTokens.application.dto.AcessTokensDto;
import com.onboarding.accessTokens.domain.model.AcessTokens;
import com.onboarding.accessTokens.domain.outport.AccessTokensRepositoryPort;
import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
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

@Component
@RequiredArgsConstructor
public class ValidateTokenCommandHandler implements CommandHandler<ValidateTokenCommand, AcessTokensDto.ValidateResponse> {

    private final AccessTokensRepositoryPort tokenPort;
    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;

    @Override
    @Transactional
    public AcessTokensDto.ValidateResponse handle(ValidateTokenCommand command) {
        String tokenHash = hashToken(command.getRawToken());

        AcessTokens token = tokenPort.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Token no válido."));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Este enlace ha caducado. Contacta con RRHH.");
        }

        OnboardingProc process = processPort.findById(token.getOnboardingProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        boolean alreadyFinished = process.getStatus() == OnboardingStatus.SUBMITTED
                || process.getStatus() == OnboardingStatus.VALIDATED
                || process.getStatus() == OnboardingStatus.REJECTED;

        if (token.getIsUsed() && alreadyFinished) {
            throw new IllegalArgumentException("Este enlace ya ha sido utilizado.");
        }

        if (!token.getIsUsed()) {
            token.setIsUsed(true);
            token.setUsedAt(LocalDateTime.now());
            tokenPort.save(token);
        }

        if (process.getStatus() == OnboardingStatus.PENDING_ACCESS
                || process.getStatus() == OnboardingStatus.REJECTED) {
            process.setStatus(OnboardingStatus.IN_PROGRESS);
            processPort.save(process);
        }

        Candidates candidate = candidatesPort.findById(process.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("Candidato no encontrado."));

        return AcessTokensDto.ValidateResponse.builder()
                .processId(process.getId())
                .candidateEmail(candidate.getEmail())
                .candidateFirstName(candidate.getFirstName())
                .candidateLastName(candidate.getLastName())
                .status(process.getStatus().name())
                .build();
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
