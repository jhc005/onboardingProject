package com.onboarding.accessTokens.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AcessTokensDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateRequest {
        private String token;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateResponse {
        private String processId;
        private String candidateEmail;
        private String candidateFirstName;
        private String candidateLastName;
        private String status;
    }
}
