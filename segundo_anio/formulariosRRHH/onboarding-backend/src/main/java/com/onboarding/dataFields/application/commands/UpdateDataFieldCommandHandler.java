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
public class UpdateDataFieldCommandHandler implements CommandHandler<UpdateDataFieldCommand, DataFieldsDto.Response> {

    private final DataFieldsRepositoryPort dataFieldsPort;
    private final OnboardingProcRepositoryPort onboardingProcPort;
    private final DataFieldsMapper mapper;

    @Override
    public DataFieldsDto.Response handle(UpdateDataFieldCommand command) {
        if (onboardingProcPort.existsActiveProcesses()) {
            throw new IllegalStateException("No se pueden modificar campos mientras haya procesos onboarding activos.");
        }
        DataFields field = dataFieldsPort.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Campo no encontrado."));
        fillFromRequest(field, command.getRequest());
        return mapper.toResponse(dataFieldsPort.save(field));
    }

    private void fillFromRequest(DataFields field, DataFieldsDto.Request request) {
        field.setLabel(request.getLabel());
        field.setFieldType(FieldType.valueOf(request.getFieldType()));
        field.setPlaceholder(request.getPlaceholder());
        field.setIsRequired(request.getIsRequired() != null ? request.getIsRequired() : true);
        field.setDisplayOrder(request.getDisplayOrder());
    }
}
