package com.onboarding.dataFields.application.commands;

import com.onboarding.dataFields.application.dto.DataFieldsDto;
import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateDataFieldCommand implements Command {
    private final Long id;
    private final DataFieldsDto.Request request;
}
