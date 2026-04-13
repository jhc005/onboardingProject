package com.onboarding.fieldOptions.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateFieldOptionCommand implements Command {
    private final Long dataFieldId;
    private final String optionLabel;
    private final String optionValue;
    private final Integer displayOrder;
}
