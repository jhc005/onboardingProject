package com.onboarding.fieldOptions.mapper;

import com.onboarding.fieldOptions.application.dto.FieldOptionsDto;
import com.onboarding.fieldOptions.domain.model.FieldOptions;
import com.onboarding.fieldOptions.infrastructure.output.jpa.FieldOptionsJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FieldOptionsMapper {

    public FieldOptions toDomain(FieldOptionsJpaEntity entity) {
        FieldOptions domain = new FieldOptions();
        domain.setId(entity.getId());
        domain.setDataFieldId(entity.getDataField() != null ? entity.getDataField().getId() : null);
        domain.setOptionLabel(entity.getOptionLabel());
        domain.setOptionValue(entity.getOptionValue());
        domain.setDisplayOrder(entity.getDisplayOrder());
        return domain;
    }

    public FieldOptionsJpaEntity toEntity(FieldOptions domain) {
        FieldOptionsJpaEntity entity = new FieldOptionsJpaEntity();
        entity.setId(domain.getId());
        entity.setOptionLabel(domain.getOptionLabel());
        entity.setOptionValue(domain.getOptionValue());
        entity.setDisplayOrder(domain.getDisplayOrder());
        return entity;
    }

    public FieldOptionsDto.Response toResponse(FieldOptions domain) {
        return FieldOptionsDto.Response.builder()
                .id(domain.getId())
                .dataFieldId(domain.getDataFieldId())
                .optionLabel(domain.getOptionLabel())
                .optionValue(domain.getOptionValue())
                .displayOrder(domain.getDisplayOrder())
                .build();
    }
}
