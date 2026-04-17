package com.onboarding.documentRequirements.application.commands;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.documentRequirements.mapper.DocumentsReqMapper;
import com.onboarding.onboardingProcesses.domain.outport.OnboardingProcRepositoryPort;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateDocumentRequirementCommandHandler implements CommandHandler<CreateDocumentRequirementCommand, DocumentsReqDto.Response> {

    private final DocumentsReqRepository documentsReqPort;
    private final OnboardingProcRepositoryPort onboardingProcPort;
    private final DocumentsReqMapper mapper;

    @Override
    public DocumentsReqDto.Response handle(CreateDocumentRequirementCommand command) {
        if (onboardingProcPort.existsActiveProcesses()) {
            throw new IllegalStateException("No se pueden crear requisitos de documentos mientras haya procesos de onboarding activos.");
        }
        DocumentsReq req = new DocumentsReq();
        fillFromRequest(req, command.getRequest());
        return mapper.toResponse(documentsReqPort.save(req));
    }

    private void fillFromRequest(DocumentsReq req, DocumentsReqDto.Request request) {
        req.setName(request.getName());
        req.setDescription(request.getDescription());
        req.setAllowedFormats(request.getAllowedFormats());
        req.setIsRequired(request.getIsRequired() != null ? request.getIsRequired() : true);
        req.setDisplayOrder(request.getDisplayOrder());
    }
}
