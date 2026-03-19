package com.onboarding.candidates.domain.outport;

import com.onboarding.candidates.domain.model.Candidates;

import java.util.List;
import java.util.Optional;

public interface CandidatesRepositoryPort {

    Candidates save(Candidates candidate);
    Optional<Candidates> findById(String id);
    Optional<Candidates> findByEmail(String email);
    List<Candidates> findAll();
    void deleteById(String id);
    boolean existsByEmail(String email);
}
