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
public class UpdateDocumentRequirementCommandHandler implements CommandHandler<UpdateDocumentRequirementCommand, DocumentsReqDto.Response> {

    private final DocumentsReqRepository documentsReqPort;
    private final OnboardingProcRepositoryPort onboardingProcPort;
    private final DocumentsReqMapper mapper;

    @Override
    public DocumentsReqDto.Response handle(UpdateDocumentRequirementCommand command) {
        if (onboardingProcPort.existsActiveProcesses()) {
            throw new IllegalStateException("No se pueden modificar requisitos de documentos mientras haya procesos de onboarding activos.");
        }
        DocumentsReq req = documentsReqPort.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado."));
        fillFromRequest(req, command.getRequest());
        return mapper.toResponse(documentsReqPort.save(req));
    }

    private void fillFromRequest(DocumentsReq req, DocumentsReqDto.Request request) {
        req.setName(request.getName());
        req.setDescription(request.getDescription());
        req.setAllowedFormats(request.getAllowedFormats());
        req.setFileNameRegex(request.getFileNameRegex());
        req.setIsRequired(request.getIsRequired() != null ? request.getIsRequired() : true);
        req.setIsConditional(request.getIsConditional() != null ? request.getIsConditional() : false);
        req.setConditionDescription(request.getConditionDescription());
        req.setDisplayOrder(request.getDisplayOrder());
    }
}
