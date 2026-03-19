package com.onboarding.fieldOptions.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FieldOptionsDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long dataFieldId;
        private String optionLabel;
        private String optionValue;
        private Integer displayOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long dataFieldId;
        private String optionLabel;
        private String optionValue;
        private Integer displayOrder;
    }
}
