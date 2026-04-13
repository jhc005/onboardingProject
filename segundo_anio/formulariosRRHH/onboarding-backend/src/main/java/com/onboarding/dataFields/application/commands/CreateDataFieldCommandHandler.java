package com.onboarding.dataFields.application.commands;

import com.onboarding.dataFields.application.dto.DataFieldsDto;
import com.onboarding.dataFields.domain.model.DataFields;
import com.onboarding.dataFields.domain.outport.DataFieldsRepositoryPort;
import com.onboarding.dataFields.domain.valueobject.FieldType;
import com.onboarding.dataFields.mapper.DataFieldsMapper;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateDataFieldCommandHandler implements CommandHandler<CreateDataFieldCommand, DataFieldsDto.Response> {

    private final DataFieldsRepositoryPort dataFieldsPort;
    private final OnboardingProcRepositoryPort onboardingProcPort;
    private final DataFieldsMapper mapper;

    @Override
    public DataFieldsDto.Response handle(CreateDataFieldCommand command) {
        if (onboardingProcPort.existsActiveProcesses()) {
            throw new IllegalStateException("No se pueden crear campos mientras haya procesos onboarding activos.");
        }
        DataFields field = new DataFields();
        fillFromRequest(field, command.getRequest());
        return mapper.toResponse(dataFieldsPort.save(field));
    }

    private void fillFromRequest(DataFields field, DataFieldsDto.Request request) {
        field.setLabel(request.getLabel());
        field.setFieldType(FieldType.valueOf(request.getFieldType()));
        field.setValidationRegex(request.getValidationRegex());
        field.setMinLength(request.getMinLength());
        field.setMaxLength(request.getMaxLength());
        field.setPlaceholder(request.getPlaceholder());
        field.setIsRequired(request.getIsRequired() != null ? request.getIsRequired() : true);
        field.setDisplayOrder(request.getDisplayOrder());
    }
}
