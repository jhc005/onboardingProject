package com.onboarding.fieldOptions.application.commands;

import com.onboarding.fieldOptions.application.dto.FieldOptionsDto;
import com.onboarding.fieldOptions.domain.model.FieldOptions;
import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import com.onboarding.fieldOptions.mapper.FieldOptionsMapper;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateFieldOptionCommandHandler implements CommandHandler<CreateFieldOptionCommand, FieldOptionsDto.Response> {

    private final FieldOptionsRepositoryPort fieldOptionsPort;
    private final FieldOptionsMapper mapper;

    @Override
    public FieldOptionsDto.Response handle(CreateFieldOptionCommand command) {
        FieldOptions option = new FieldOptions();
        option.setDataFieldId(command.getDataFieldId());
        option.setOptionLabel(command.getOptionLabel());
        option.setOptionValue(command.getOptionValue());
        option.setDisplayOrder(command.getDisplayOrder());
        return mapper.toResponse(fieldOptionsPort.save(option));
    }
}
