package com.onboarding.dataFields.domain.model;

import com.onboarding.dataFields.domain.valueobject.FieldType;

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
    private String placeholder;
    private Boolean isRequired;
    private Integer displayOrder;
}
