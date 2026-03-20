package com.onboarding.dataFields.mapper;

import com.onboarding.dataFields.application.dto.DataFieldsDto;
import com.onboarding.dataFields.domain.model.DataFields;
import com.onboarding.dataFields.domain.valueobject.FieldType;
import com.onboarding.dataFields.infrastructure.output.jpa.DataFieldsJpaEntity;
import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataFieldsMapper {

        private final FieldOptionsRepositoryPort fieldOptionsPort;

        public DataFields toDomain(DataFieldsJpaEntity entity) {
                DataFields domain = new DataFields();
                domain.setId(entity.getId());
                domain.setLabel(entity.getLabel());
                domain.setFieldType(entity.getFieldType() != null
                                ? FieldType.valueOf(entity.getFieldType().name())
                                : null);
                domain.setValidationRegex(entity.getValidationRegex());
                domain.setMinLength(entity.getMinLength());
                domain.setMaxLength(entity.getMaxLength());
                domain.setPlaceholder(entity.getPlaceholder());
                domain.setIsRequired(entity.getIsRequired());
                domain.setDisplayOrder(entity.getDisplayOrder());
                return domain;
        }

        public DataFieldsJpaEntity toEntity(DataFields domain) {
                DataFieldsJpaEntity entity = new DataFieldsJpaEntity();
                entity.setId(domain.getId());
                entity.setLabel(domain.getLabel());
                entity.setFieldType(domain.getFieldType() != null
                                ? FieldType.valueOf(domain.getFieldType().name())
                                : null);
                entity.setValidationRegex(domain.getValidationRegex());
                entity.setMinLength(domain.getMinLength());
                entity.setMaxLength(domain.getMaxLength());
                entity.setPlaceholder(domain.getPlaceholder());
                entity.setIsRequired(domain.getIsRequired());
                entity.setDisplayOrder(domain.getDisplayOrder());
                return entity;
        }

        public DataFieldsDto.Response toResponse(DataFields field) {
                List<DataFieldsDto.Response.FieldOptionItem> options = fieldOptionsPort
                                .findByDataFieldId(field.getId())
                                .stream()
                                .map(o -> DataFieldsDto.Response.FieldOptionItem.builder()
                                                .id(o.getId())
                                                .optionLabel(o.getOptionLabel())
                                                .optionValue(o.getOptionValue())
                                                .displayOrder(o.getDisplayOrder())
                                                .build())
                                .toList();

                return DataFieldsDto.Response.builder()
                                .id(field.getId())
                                .label(field.getLabel())
                                .fieldType(field.getFieldType() != null ? field.getFieldType().name() : null)
                                .validationRegex(field.getValidationRegex())
                                .minLength(field.getMinLength())
                                .maxLength(field.getMaxLength())
                                .placeholder(field.getPlaceholder())
                                .isRequired(field.getIsRequired())
                                .displayOrder(field.getDisplayOrder())
                                .options(options)
                                .build();
        }
}
