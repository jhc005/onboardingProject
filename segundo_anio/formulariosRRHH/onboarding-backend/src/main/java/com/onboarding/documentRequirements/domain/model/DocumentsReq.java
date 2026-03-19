package com.onboarding.documentRequirements.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentsReq {

    private Long id;
    private String name;
    private String description;
    private String allowedFormats;
    private String fileNameRegex;
    private Boolean isRequired;
    private Boolean isConditional;
    private String conditionDescription;
    private Integer displayOrder;
}
