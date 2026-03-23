package com.onboarding.documentRequirements.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DocumentsReqDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String description;
        private String allowedFormats;
        private String fileNameRegex;
        private Boolean isRequired;
        private Boolean isConditional;
        private String conditionDescription;
        private Integer displayOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private String allowedFormats;
        private String fileNameRegex;
        private Boolean isRequired;
        private Boolean isConditional;
        private String conditionDescription;
        private Integer displayOrder;
        private String templateOriginalFilename;
        private Boolean hasTemplate;
    }
}
