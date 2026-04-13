package com.onboarding.fieldOptions.application.commands;

import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteFieldOptionCommandHandler implements CommandHandler<DeleteFieldOptionCommand, Void> {

    private final FieldOptionsRepositoryPort fieldOptionsPort;

    @Override
    public Void handle(DeleteFieldOptionCommand command) {
        fieldOptionsPort.deleteById(command.getId());
        return null;
    }
}
