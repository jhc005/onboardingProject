package com.onboarding.accessTokens.mapper;

import com.onboarding.accessTokens.domain.model.AcessTokens;
import com.onboarding.accessTokens.infrastructure.output.jpa.AcessTokensJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AcessTokensMapper {

    public AcessTokens toDomain(AcessTokensJpaEntity entity) {
        AcessTokens domain = new AcessTokens();
        domain.setId(entity.getId());
        domain.setOnboardingProcessId(entity.getOnboardingProcess() != null
                ? entity.getOnboardingProcess().getId() : null);
        domain.setTokenHash(entity.getTokenHash());
        domain.setExpiresAt(entity.getExpiresAt());
        domain.setUsedAt(entity.getUsedAt());
        domain.setIsUsed(entity.getIsUsed());
        domain.setAttempts(entity.getAttempts());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }

    public AcessTokensJpaEntity toEntity(AcessTokens domain) {
        AcessTokensJpaEntity entity = new AcessTokensJpaEntity();
        entity.setId(domain.getId());
        entity.setTokenHash(domain.getTokenHash());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUsedAt(domain.getUsedAt());
        entity.setIsUsed(domain.getIsUsed());
        entity.setAttempts(domain.getAttempts());
        return entity;
    }
}
