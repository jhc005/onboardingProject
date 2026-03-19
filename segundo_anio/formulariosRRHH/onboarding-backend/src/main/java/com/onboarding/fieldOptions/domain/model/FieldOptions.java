package com.onboarding.fieldOptions.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldOptions {

    private Long id;
    private Long dataFieldId;
    private String optionLabel;
    private String optionValue;
    private Integer displayOrder;
}
