package com.onboarding.documentRequirements.infrastructure.output.adapter;

import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.documentRequirements.infrastructure.output.jpa.DocumentsReqJpaRepository;
import com.onboarding.documentRequirements.mapper.DocumentsReqMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DocumentsReqAdapter implements DocumentsReqRepository {

    private final DocumentsReqJpaRepository repository;
    private final DocumentsReqMapper mapper;

    @Override
    public DocumentsReq save(DocumentsReq requirement) {
        return mapper.toDomain(repository.save(mapper.toEntity(requirement)));
    }

    @Override
    public Optional<DocumentsReq> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<DocumentsReq> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<DocumentsReq> findAllOrderedByDisplayOrder() {
        return repository.findAllByOrderByDisplayOrderAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<DocumentsReq> findByIsRequired(Boolean isRequired) {
        return repository.findByIsRequired(isRequired).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
