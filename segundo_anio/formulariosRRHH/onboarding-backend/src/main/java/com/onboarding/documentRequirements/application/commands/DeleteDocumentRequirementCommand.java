package com.onboarding.documentRequirements.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteDocumentRequirementCommand implements Command {
    private final Long id;
}
