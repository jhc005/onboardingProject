package com.onboarding.onboardingProcesses.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class OnboardingProcDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String id;
        private String candidateId;
        private String candidateEmail;
        private String candidateFirstName;
        private String candidateLastName;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime submittedAt;
        private LocalDateTime validatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RejectRequest {
        private String notes;
    }
}
