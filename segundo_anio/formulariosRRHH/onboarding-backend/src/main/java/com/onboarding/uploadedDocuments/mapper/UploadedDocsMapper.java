package com.onboarding.uploadedDocuments.mapper;

import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import com.onboarding.uploadedDocuments.domain.model.UploadedDocs;
import com.onboarding.uploadedDocuments.infrastructure.output.jpa.UploadedDocsJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UploadedDocsMapper {

    private final DocumentsReqRepository documentsReqPort;

    public UploadedDocs toDomain(UploadedDocsJpaEntity entity) {
        UploadedDocs domain = new UploadedDocs();
        domain.setId(entity.getId());
        domain.setOnboardingProcessId(entity.getOnboardingProcess() != null
                ? entity.getOnboardingProcess().getId() : null);
        domain.setDocumentRequirementId(entity.getDocumentRequirement() != null
                ? entity.getDocumentRequirement().getId() : null);
        domain.setFilePath(entity.getFilePath());
        domain.setOriginalFilename(entity.getOriginalFilename());
        domain.setMimeType(entity.getMimeType());
        domain.setFileSizeBytes(entity.getFileSizeBytes());
        domain.setValidationStatus(entity.getValidationStatus() != null
                ? UploadedDocs.ValidationStatus.valueOf(entity.getValidationStatus().name()) : null);
        domain.setValidationNotes(entity.getValidationNotes());
        domain.setUploadedAt(entity.getUploadedAt());
        return domain;
    }

    public UploadedDocsJpaEntity toEntity(UploadedDocs domain) {
        UploadedDocsJpaEntity entity = new UploadedDocsJpaEntity();
        entity.setId(domain.getId());
        entity.setFilePath(domain.getFilePath());
        entity.setOriginalFilename(domain.getOriginalFilename());
        entity.setMimeType(domain.getMimeType());
        entity.setFileSizeBytes(domain.getFileSizeBytes());
        entity.setValidationStatus(domain.getValidationStatus() != null
                ? UploadedDocsJpaEntity.ValidationStatus.valueOf(domain.getValidationStatus().name()) : null);
        entity.setValidationNotes(domain.getValidationNotes());
        return entity;
    }

    public UploadedDocsDto.Response toResponse(UploadedDocs doc) {
        String requirementName = documentsReqPort.findById(doc.getDocumentRequirementId())
                .map(DocumentsReq::getName)
                .orElse("");

        return UploadedDocsDto.Response.builder()
                .id(doc.getId())
                .onboardingProcessId(doc.getOnboardingProcessId())
                .documentRequirementId(doc.getDocumentRequirementId())
                .requirementName(requirementName)
                .originalFilename(doc.getOriginalFilename())
                .mimeType(doc.getMimeType())
                .fileSizeBytes(doc.getFileSizeBytes())
                .validationStatus(doc.getValidationStatus() != null ? doc.getValidationStatus().name() : null)
                .validationNotes(doc.getValidationNotes())
                .uploadedAt(doc.getUploadedAt())
                .build();
    }
}
