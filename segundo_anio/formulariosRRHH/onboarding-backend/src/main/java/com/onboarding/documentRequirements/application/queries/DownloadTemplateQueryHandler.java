package com.onboarding.documentRequirements.application.queries;

import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class DownloadTemplateQueryHandler implements QueryHandler<DownloadTemplateQuery, DownloadTemplateQuery.Result> {

    private final DocumentsReqRepository documentsReqPort;

    @Override
    public DownloadTemplateQuery.Result handle(DownloadTemplateQuery query) {
        DocumentsReq req = documentsReqPort.findById(query.getId())
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado."));

        if (req.getTemplateFilePath() == null) {
            throw new IllegalArgumentException("Este requisito no tiene plantilla.");
        }

        try {
            Path filePath = Paths.get(req.getTemplateFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new IllegalArgumentException("Archivo de plantilla no encontrado en disco.");
            }

            return new DownloadTemplateQuery.Result(resource, req.getTemplateOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Error al acceder a la plantilla.", e);
        }
    }
}
