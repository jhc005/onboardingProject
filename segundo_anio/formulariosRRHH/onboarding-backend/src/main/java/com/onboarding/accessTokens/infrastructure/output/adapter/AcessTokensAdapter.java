package com.onboarding.accessTokens.infrastructure.output.adapter;

import com.onboarding.accessTokens.domain.model.AcessTokens;
import com.onboarding.accessTokens.domain.outport.AccessTokensRepositoryPort;
import com.onboarding.accessTokens.infrastructure.output.jpa.AcessTokensJpaEntity;
import com.onboarding.accessTokens.infrastructure.output.jpa.AcessTokensJpaRepository;
import com.onboarding.accessTokens.mapper.AcessTokensMapper;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AcessTokensAdapter implements AccessTokensRepositoryPort {

    private final AcessTokensJpaRepository repository;
    private final OnboardingProcJpaRepository processRepository;
    private final AcessTokensMapper mapper;

    @Override
    public AcessTokens save(AcessTokens token) {
        AcessTokensJpaEntity entity = mapper.toEntity(token);
        if (token.getOnboardingProcessId() != null) {
            processRepository.findById(token.getOnboardingProcessId()).ifPresent(entity::setOnboardingProcess);
        }
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<AcessTokens> findByTokenHash(String tokenHash) {
        return repository.findByTokenHash(tokenHash).map(mapper::toDomain);
    }

    @Override
    public Optional<AcessTokens> findByOnboardingProcessId(String processId) {
        return repository.findByOnboardingProcess_Id(processId).map(mapper::toDomain);
    }

    @Override
    public void deleteByOnboardingProcessId(String processId) {
        repository.deleteByOnboardingProcess_Id(processId);
    }
}
