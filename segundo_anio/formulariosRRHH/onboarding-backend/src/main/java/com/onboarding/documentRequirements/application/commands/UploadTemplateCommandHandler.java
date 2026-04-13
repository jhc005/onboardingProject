package com.onboarding.documentRequirements.application.commands;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.documentRequirements.mapper.DocumentsReqMapper;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UploadTemplateCommandHandler implements CommandHandler<UploadTemplateCommand, DocumentsReqDto.Response> {

    private final DocumentsReqRepository documentsReqPort;
    private final DocumentsReqMapper mapper;

    @Value("${app.storage.path}")
    private String storagePath;

    @Override
    public DocumentsReqDto.Response handle(UploadTemplateCommand command) {
        DocumentsReq req = documentsReqPort.findById(command.getId())
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado."));

        try {
            String storedFilename = UUID.randomUUID() + "_" + command.getFile().getOriginalFilename();
            Path dest = Paths.get(storagePath, "templates");
            Files.createDirectories(dest);
            Files.copy(command.getFile().getInputStream(), dest.resolve(storedFilename));

            req.setTemplateFilePath(dest.resolve(storedFilename).toString());
            req.setTemplateOriginalFilename(command.getFile().getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la plantilla.", e);
        }

        return mapper.toResponse(documentsReqPort.save(req));
    }
}
