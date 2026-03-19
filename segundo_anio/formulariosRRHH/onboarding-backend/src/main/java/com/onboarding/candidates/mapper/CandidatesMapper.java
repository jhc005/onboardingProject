package com.onboarding.candidates.mapper;

import com.onboarding.candidates.application.dto.CandidatesDto;
import com.onboarding.candidates.domain.model.Candidates;
import com.onboarding.candidates.infrastructure.output.jpa.CandidatesJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CandidatesMapper {

    public Candidates toDomain(CandidatesJpaEntity entity) {
        Candidates domain = new Candidates();
        domain.setId(entity.getId());
        domain.setEmail(entity.getEmail());
        domain.setFirstName(entity.getFirstName());
        domain.setLastName(entity.getLastName());
        domain.setCreatedAt(entity.getCreatedAt());
        return domain;
    }

    public CandidatesJpaEntity toEntity(Candidates domain) {
        CandidatesJpaEntity entity = new CandidatesJpaEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        return entity;
    }

    public CandidatesDto.Response toResponse(Candidates candidate) {
        return CandidatesDto.Response.builder()
                .id(candidate.getId())
                .email(candidate.getEmail())
                .firstName(candidate.getFirstName())
                .lastName(candidate.getLastName())
                .createdAt(candidate.getCreatedAt())
                .build();
    }
}
