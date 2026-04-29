package com.onboarding.uploadedDocuments.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UploadedDocsDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String onboardingProcessId;
        private Long documentRequirementId;
        private String requirementName;
        private String originalFilename;
        private String mimeType;
        private Long fileSizeBytes;
        private String validationStatus;
        private String validationNotes;
        private LocalDateTime uploadedAt;
    }

}
