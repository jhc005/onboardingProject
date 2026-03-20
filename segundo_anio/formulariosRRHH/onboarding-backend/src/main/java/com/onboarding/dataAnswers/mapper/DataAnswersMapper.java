package com.onboarding.dataAnswers.mapper;

import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import com.onboarding.dataAnswers.domain.model.DataAnswers;
import com.onboarding.dataAnswers.infrastructure.output.jpa.DataAnswersJpaEntity;
import com.onboarding.dataFields.domain.model.DataFields;
import com.onboarding.dataFields.domain.outport.DataFieldsRepositoryPort;
import com.onboarding.fieldOptions.domain.model.FieldOptions;
import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataAnswersMapper {

        private final DataFieldsRepositoryPort dataFieldsPort;
        private final FieldOptionsRepositoryPort fieldOptionsPort;

        public DataAnswers toDomain(DataAnswersJpaEntity entity) {
                DataAnswers domain = new DataAnswers();
                domain.setId(entity.getId());
                domain.setOnboardingProcessId(entity.getOnboardingProcess() != null
                                ? entity.getOnboardingProcess().getId()
                                : null);
                domain.setDataFieldId(entity.getDataField() != null
                                ? entity.getDataField().getId()
                                : null);
                domain.setAnswerValue(entity.getAnswerValue());
                domain.setFieldOptionId(entity.getFieldOption() != null
                                ? entity.getFieldOption().getId()
                                : null);
                domain.setCreatedAt(entity.getCreatedAt());
                return domain;
        }

        public DataAnswersJpaEntity toEntity(DataAnswers domain) {
                DataAnswersJpaEntity entity = new DataAnswersJpaEntity();
                entity.setId(domain.getId());
                entity.setAnswerValue(domain.getAnswerValue());
                return entity;
        }

        public DataAnswersDto.Response toResponse(DataAnswers answer) {
                DataFields field = answer.getDataFieldId() != null
                                ? dataFieldsPort.findById(answer.getDataFieldId()).orElse(null)
                                : null;
                FieldOptions option = answer.getFieldOptionId() != null
                                ? fieldOptionsPort.findById(answer.getFieldOptionId()).orElse(null)
                                : null;

                return DataAnswersDto.Response.builder()
                                .id(answer.getId())
                                .onboardingProcessId(answer.getOnboardingProcessId())
                                .dataFieldId(answer.getDataFieldId())
                                .fieldLabel(field != null ? field.getLabel() : null)
                                .answerValue(answer.getAnswerValue())
                                .fieldOptionId(answer.getFieldOptionId())
                                .optionLabel(option != null ? option.getOptionLabel() : null)
                                .createdAt(answer.getCreatedAt())
                                .build();
        }
}
