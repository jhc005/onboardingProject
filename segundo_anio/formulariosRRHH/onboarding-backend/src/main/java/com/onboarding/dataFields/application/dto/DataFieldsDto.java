package com.onboarding.dataFields.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class DataFieldsDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String label;
        private String fieldType;
        private String placeholder;
        private Boolean isRequired;
        private Integer displayOrder;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String label;
        private String fieldType;
        private String placeholder;
        private Boolean isRequired;
        private Integer displayOrder;
        private List<FieldOptionItem> options;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class FieldOptionItem {
            private Long id;
            private String optionLabel;
            private String optionValue;
            private Integer displayOrder;
        }
    }
}
