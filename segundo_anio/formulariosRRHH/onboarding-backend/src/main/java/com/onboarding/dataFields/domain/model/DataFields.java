package com.onboarding.dataFields.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataFields {

    private Long id;
    private String label;
    private FieldType fieldType;
    private String validationRegex;
    private Integer minLength;
    private Integer maxLength;
    private String placeholder;
    private Boolean isRequired;
    private Integer displayOrder;

    public enum FieldType {
        TEXT, NUMBER, SELECT, TEXTAREA, RADIO
    }
}
