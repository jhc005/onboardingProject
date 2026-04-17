package com.onboarding.dataAnswers.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class DataAnswersDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerItem {
        private Long dataFieldId;
        private String answerValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequest {
        private List<AnswerItem> answers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String onboardingProcessId;
        private Long dataFieldId;
        private String fieldLabel;
        private String answerValue;
        private LocalDateTime createdAt;
    }
}
