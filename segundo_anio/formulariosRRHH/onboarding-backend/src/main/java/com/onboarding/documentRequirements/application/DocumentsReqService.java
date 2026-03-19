package com.onboarding.documentRequirements.application;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.documentRequirements.mapper.DocumentsReqMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentsReqService {

    private final DocumentsReqRepository documentsReqPort;
    private final DocumentsReqMapper mapper;

    public List<DocumentsReqDto.Response> findAll() {
        return documentsReqPort.findAllOrderedByDisplayOrder().stream().map(mapper::toResponse).toList();
    }

    public DocumentsReqDto.Response findById(Long id) {
        return documentsReqPort.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Requisito de documento no encontrado."));
    }

    public DocumentsReqDto.Response create(DocumentsReqDto.Request request) {
        DocumentsReq req = new DocumentsReq();
        fillFromRequest(req, request);
        return mapper.toResponse(documentsReqPort.save(req));
    }

    public DocumentsReqDto.Response update(Long id, DocumentsReqDto.Request request) {
        DocumentsReq req = documentsReqPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado."));
        fillFromRequest(req, request);
        return mapper.toResponse(documentsReqPort.save(req));
    }

    public void delete(Long id) {
        documentsReqPort.deleteById(id);
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
