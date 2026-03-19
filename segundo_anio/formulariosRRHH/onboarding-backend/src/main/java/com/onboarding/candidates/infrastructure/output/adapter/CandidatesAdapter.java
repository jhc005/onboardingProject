package com.onboarding.candidates.infrastructure.output.adapter;

import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.candidates.infrastructure.output.jpa.CandidatesJpaRepository;
import com.onboarding.candidates.mapper.CandidatesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CandidatesAdapter implements CandidatesRepositoryPort {

    private final CandidatesJpaRepository repository;
    private final CandidatesMapper mapper;

    @Override
    public Candidates save(Candidates candidate) {
        return mapper.toDomain(repository.save(mapper.toEntity(candidate)));
    }

    @Override
    public Optional<Candidates> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Candidates> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Candidates> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
