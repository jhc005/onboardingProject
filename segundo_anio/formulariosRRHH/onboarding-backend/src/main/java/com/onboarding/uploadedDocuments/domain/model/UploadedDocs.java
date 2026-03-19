package com.onboarding.uploadedDocuments.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadedDocs {

    private Long id;
    private String onboardingProcessId;
    private Long documentRequirementId;
    private String filePath;
    private String originalFilename;
    private String mimeType;
    private Long fileSizeBytes;
    private ValidationStatus validationStatus;
    private String validationNotes;
    private LocalDateTime uploadedAt;

    public enum ValidationStatus {
        PENDING_VALIDATION, APPROVED, REJECTED
    }
}
