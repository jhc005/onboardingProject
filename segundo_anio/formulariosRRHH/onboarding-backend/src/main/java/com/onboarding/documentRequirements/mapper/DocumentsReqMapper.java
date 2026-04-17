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
        domain.setIsRequired(entity.getIsRequired());
        domain.setDisplayOrder(entity.getDisplayOrder());
        domain.setTemplateFilePath(entity.getTemplateFilePath());
        domain.setTemplateOriginalFilename(entity.getTemplateOriginalFilename());
        return domain;
    }

    public DocumentsReqJpaEntity toEntity(DocumentsReq domain) {
        DocumentsReqJpaEntity entity = new DocumentsReqJpaEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setAllowedFormats(domain.getAllowedFormats());
        entity.setIsRequired(domain.getIsRequired());
        entity.setDisplayOrder(domain.getDisplayOrder());
        entity.setTemplateFilePath(domain.getTemplateFilePath());
        entity.setTemplateOriginalFilename(domain.getTemplateOriginalFilename());
        return entity;
    }

    public DocumentsReqDto.Response toResponse(DocumentsReq req) {
        return DocumentsReqDto.Response.builder()
                .id(req.getId())
                .name(req.getName())
                .description(req.getDescription())
                .allowedFormats(req.getAllowedFormats())
                .isRequired(req.getIsRequired())
                .displayOrder(req.getDisplayOrder())
                .templateOriginalFilename(req.getTemplateOriginalFilename())
                .hasTemplate(req.getTemplateFilePath() != null)
                .build();
    }
}
