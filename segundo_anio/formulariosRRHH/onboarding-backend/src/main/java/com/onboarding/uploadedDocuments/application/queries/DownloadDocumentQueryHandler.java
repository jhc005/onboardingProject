package com.onboarding.uploadedDocuments.application.queries;

import com.onboarding.shared.cqrs.QueryHandler;
import com.onboarding.uploadedDocuments.domain.model.UploadedDocs;
import com.onboarding.uploadedDocuments.domain.outport.UploadedDocsRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class DownloadDocumentQueryHandler implements QueryHandler<DownloadDocumentQuery, DownloadDocumentQuery.Result> {

    private final UploadedDocsRepositoryPort uploadedDocsPort;

    @Override
    public DownloadDocumentQuery.Result handle(DownloadDocumentQuery query) {
        UploadedDocs doc = uploadedDocsPort.findById(query.getDocId())
                .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado."));

        try {
            Path path = Paths.get(doc.getFilePath());
            Resource resource = new UrlResource(path.toUri());
            return new DownloadDocumentQuery.Result(resource, doc.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Error al acceder al documento.", e);
        }
    }
}
