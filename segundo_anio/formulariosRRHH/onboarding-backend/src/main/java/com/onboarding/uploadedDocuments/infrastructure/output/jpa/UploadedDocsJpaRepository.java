package com.onboarding.uploadedDocuments.infrastructure.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UploadedDocsJpaRepository extends JpaRepository<UploadedDocsJpaEntity, Long> {

    List<UploadedDocsJpaEntity> findByOnboardingProcess_Id(String processId);
    Optional<UploadedDocsJpaEntity> findByOnboardingProcess_IdAndDocumentRequirement_Id(String processId, Long requirementId);
    List<UploadedDocsJpaEntity> findByOnboardingProcess_IdAndValidationStatus(String processId, UploadedDocsJpaEntity.ValidationStatus status);
}
