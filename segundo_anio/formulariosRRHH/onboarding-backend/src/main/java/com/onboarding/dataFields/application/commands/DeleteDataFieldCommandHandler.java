package com.onboarding.dataFields.application.commands;

import com.onboarding.dataAnswers.domain.outport.DataAnswersRepositoryPort;
import com.onboarding.dataFields.domain.outport.DataFieldsRepositoryPort;
import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteDataFieldCommandHandler implements CommandHandler<DeleteDataFieldCommand, Void> {

    private final DataFieldsRepositoryPort dataFieldsPort;
    private final FieldOptionsRepositoryPort fieldOptionsPort;
    private final DataAnswersRepositoryPort dataAnswersPort;
    private final OnboardingProcRepositoryPort onboardingProcPort;

    @Override
    @Transactional
    public Void handle(DeleteDataFieldCommand command) {
        if (onboardingProcPort.existsActiveProcesses()) {
            throw new IllegalStateException("No se pueden eliminar campos mientras haya procesos onboarding activos.");
        }
        dataAnswersPort.deleteByDataFieldId(command.getId());
        fieldOptionsPort.deleteByDataFieldId(command.getId());
        dataFieldsPort.deleteById(command.getId());
        return null;
    }
}
