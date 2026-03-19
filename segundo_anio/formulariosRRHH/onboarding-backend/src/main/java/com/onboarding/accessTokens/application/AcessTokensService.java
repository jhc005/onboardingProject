package com.onboarding.accessTokens.application;

import com.onboarding.accessTokens.application.dto.AcessTokensDto;
import com.onboarding.accessTokens.domain.model.AcessTokens;
import com.onboarding.accessTokens.domain.outport.AccessTokensRepositoryPort;
import com.onboarding.candidates.application.CandidatesService;
import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AcessTokensService {

    private final AccessTokensRepositoryPort tokenPort;
    private final OnboardingProcRepositoryPort processPort;
    private final CandidatesRepositoryPort candidatesPort;

    @Transactional
    public AcessTokensDto.ValidateResponse validateToken(String rawToken) {
        String tokenHash = CandidatesService.hashToken(rawToken);

        AcessTokens token = tokenPort.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Token no válido."));

        if (token.getIsUsed()) {
            throw new IllegalArgumentException("Este enlace ya ha sido utilizado.");
        }
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Este enlace ha caducado. Contacta con RRHH.");
        }

        token.setIsUsed(true);
        token.setUsedAt(LocalDateTime.now());
        tokenPort.save(token);

        OnboardingProc process = processPort.findById(token.getOnboardingProcessId())
                .orElseThrow(() -> new IllegalArgumentException("Proceso no encontrado."));

        if (process.getStatus() == OnboardingProc.OnboardingStatus.PENDING_ACCESS) {
            process.setStatus(OnboardingProc.OnboardingStatus.IN_PROGRESS);
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
}
