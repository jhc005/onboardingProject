package com.onboarding.dataFields.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteDataFieldCommand implements Command {
    private final Long id;
}
