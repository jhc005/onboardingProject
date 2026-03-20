package com.onboarding.onboardingProcesses.infrastructure.output.adapter;

import com.onboarding.adminUsers.infrastructure.output.jpa.AdminUsersJpaRepository;
import com.onboarding.candidates.infrastructure.output.jpa.CandidatesJpaRepository;
import com.onboarding.onboardingProcesses.domain.model.OnboardingProc;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.onboardingProcesses.domain.valueobject.OnboardingStatus;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaEntity;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaRepository;
import com.onboarding.onboardingProcesses.mapper.OnboardingProcMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OnboardingProcAdapter implements OnboardingProcRepositoryPort {

    private final OnboardingProcJpaRepository repository;
    private final CandidatesJpaRepository candidatesRepository;
    private final AdminUsersJpaRepository adminUsersRepository;
    private final OnboardingProcMapper mapper;

    @Override
    public OnboardingProc save(OnboardingProc process) {
        OnboardingProcJpaEntity entity = mapper.toEntity(process);
        if (process.getCandidateId() != null) {
            candidatesRepository.findById(process.getCandidateId()).ifPresent(entity::setCandidate);
        }
        if (process.getValidatedById() != null) {
            adminUsersRepository.findById(process.getValidatedById()).ifPresent(entity::setValidatedBy);
        }
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<OnboardingProc> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<OnboardingProc> findByCandidateId(String candidateId) {
        return repository.findByCandidate_Id(candidateId).map(mapper::toDomain);
    }

    @Override
    public List<OnboardingProc> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<OnboardingProc> findByStatus(OnboardingStatus status) {
        OnboardingStatus jpaStatus =
                OnboardingStatus.valueOf(status.name());
        return repository.findByStatus(jpaStatus).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
