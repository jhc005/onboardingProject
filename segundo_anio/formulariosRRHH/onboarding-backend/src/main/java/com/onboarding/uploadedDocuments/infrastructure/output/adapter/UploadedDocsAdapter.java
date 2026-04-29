package com.onboarding.uploadedDocuments.infrastructure.output.adapter;

import com.onboarding.documentRequirements.infrastructure.output.jpa.DocumentsReqJpaRepository;
import com.onboarding.onboardingProcesses.infrastructure.output.jpa.OnboardingProcJpaRepository;
import com.onboarding.uploadedDocuments.domain.model.UploadedDocs;
import com.onboarding.uploadedDocuments.domain.outport.UploadedDocsRepositoryPort;
import com.onboarding.uploadedDocuments.infrastructure.output.jpa.UploadedDocsJpaEntity;
import com.onboarding.uploadedDocuments.infrastructure.output.jpa.UploadedDocsJpaRepository;
import com.onboarding.uploadedDocuments.mapper.UploadedDocsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UploadedDocsAdapter implements UploadedDocsRepositoryPort {

    private final UploadedDocsJpaRepository repository;
    private final OnboardingProcJpaRepository processRepository;
    private final DocumentsReqJpaRepository requirementRepository;
    private final UploadedDocsMapper mapper;

    @Override
    public UploadedDocs save(UploadedDocs document) {
        UploadedDocsJpaEntity entity = mapper.toEntity(document);
        if (document.getOnboardingProcessId() != null) {
            processRepository.findById(document.getOnboardingProcessId()).ifPresent(entity::setOnboardingProcess);
        }
        if (document.getDocumentRequirementId() != null) {
            requirementRepository.findById(document.getDocumentRequirementId()).ifPresent(entity::setDocumentRequirement);
        }
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<UploadedDocs> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<UploadedDocs> findByOnboardingProcessId(String processId) {
        return repository.findByOnboardingProcess_Id(processId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<UploadedDocs> findByOnboardingProcessIdAndDocumentRequirementId(String processId, Long requirementId) {
        return repository.findByOnboardingProcess_IdAndDocumentRequirement_Id(processId, requirementId)
                .map(mapper::toDomain);
    }
}
