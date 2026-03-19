package com.onboarding.accessTokens.domain.outport;

import com.onboarding.accessTokens.domain.model.AcessTokens;

import java.util.Optional;

public interface AccessTokensRepositoryPort {

    AcessTokens save(AcessTokens token);
    Optional<AcessTokens> findByTokenHash(String tokenHash);
    Optional<AcessTokens> findByOnboardingProcessId(String onboardingProcessId);
    void deleteByOnboardingProcessId(String onboardingProcessId);
}
