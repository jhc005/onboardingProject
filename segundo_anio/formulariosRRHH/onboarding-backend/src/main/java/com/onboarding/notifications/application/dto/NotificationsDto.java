package com.onboarding.notifications.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NotificationsDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String onboardingProcessId;
        private String type;
        private String recipientEmail;
        private LocalDateTime sentAt;
        private String status;
        private String errorMessage;
    }
}
