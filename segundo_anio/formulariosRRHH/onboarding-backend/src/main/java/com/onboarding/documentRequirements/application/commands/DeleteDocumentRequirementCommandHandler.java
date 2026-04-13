package com.onboarding.documentRequirements.application.commands;

import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteDocumentRequirementCommandHandler implements CommandHandler<DeleteDocumentRequirementCommand, Void> {

    private final DocumentsReqRepository documentsReqPort;
    private final OnboardingProcRepositoryPort onboardingProcPort;

    @Override
    public Void handle(DeleteDocumentRequirementCommand command) {
        if (onboardingProcPort.existsActiveProcesses()) {
            throw new IllegalStateException("No se pueden eliminar requisitos de documentos mientras haya procesos de onboarding activos.");
        }
        documentsReqPort.deleteById(command.getId());
        return null;
    }
}
