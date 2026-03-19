package com.onboarding.documentRequirements.mapper;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.infrastructure.output.jpa.DocumentsReqJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class DocumentsReqMapper {

    public DocumentsReq toDomain(DocumentsReqJpaEntity entity) {
        DocumentsReq domain = new DocumentsReq();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setAllowedFormats(entity.getAllowedFormats());
        domain.setFileNameRegex(entity.getFileNameRegex());
        domain.setIsRequired(entity.getIsRequired());
        domain.setIsConditional(entity.getIsConditional());
        domain.setConditionDescription(entity.getConditionDescription());
        domain.setDisplayOrder(entity.getDisplayOrder());
        return domain;
    }

    public DocumentsReqJpaEntity toEntity(DocumentsReq domain) {
        DocumentsReqJpaEntity entity = new DocumentsReqJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setAllowedFormats(domain.getAllowedFormats());
        entity.setFileNameRegex(domain.getFileNameRegex());
        entity.setIsRequired(domain.getIsRequired());
        entity.setIsConditional(domain.getIsConditional());
        entity.setConditionDescription(domain.getConditionDescription());
        entity.setDisplayOrder(domain.getDisplayOrder());
        return entity;
    }

    public DocumentsReqDto.Response toResponse(DocumentsReq req) {
        return DocumentsReqDto.Response.builder()
                .id(req.getId())
                .name(req.getName())
                .description(req.getDescription())
                .allowedFormats(req.getAllowedFormats())
                .fileNameRegex(req.getFileNameRegex())
                .isRequired(req.getIsRequired())
                .isConditional(req.getIsConditional())
                .conditionDescription(req.getConditionDescription())
                .displayOrder(req.getDisplayOrder())
                .build();
    }
}
