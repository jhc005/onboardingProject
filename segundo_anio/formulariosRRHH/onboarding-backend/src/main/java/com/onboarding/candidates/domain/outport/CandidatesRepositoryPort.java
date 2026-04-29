package com.onboarding.candidates.domain.outport;

import com.onboarding.candidates.domain.model.Candidates;

import java.util.List;
import java.util.Optional;

public interface CandidatesRepositoryPort {

    Candidates save(Candidates candidate);
    Optional<Candidates> findById(String id);
    List<Candidates> findAll();
    boolean existsByEmail(String email);
}
