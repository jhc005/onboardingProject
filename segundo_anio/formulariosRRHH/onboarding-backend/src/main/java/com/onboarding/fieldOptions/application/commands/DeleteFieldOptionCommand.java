package com.onboarding.fieldOptions.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteFieldOptionCommand implements Command {
    private final Long id;
}
