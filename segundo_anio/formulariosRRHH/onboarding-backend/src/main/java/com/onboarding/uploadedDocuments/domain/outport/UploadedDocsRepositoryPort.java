package com.onboarding.uploadedDocuments.domain.outport;

import com.onboarding.uploadedDocuments.domain.model.UploadedDocs;

import java.util.List;
import java.util.Optional;

public interface UploadedDocsRepositoryPort {

    UploadedDocs save(UploadedDocs document);
    Optional<UploadedDocs> findById(Long id);
    List<UploadedDocs> findByOnboardingProcessId(String onboardingProcessId);
    Optional<UploadedDocs> findByOnboardingProcessIdAndDocumentRequirementId(String onboardingProcessId, Long documentRequirementId);
}
