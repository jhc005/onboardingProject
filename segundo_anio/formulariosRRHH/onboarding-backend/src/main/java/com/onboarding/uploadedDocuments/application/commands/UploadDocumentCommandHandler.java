package com.onboarding.uploadedDocuments.application.commands;

import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.shared.cqrs.CommandHandler;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import com.onboarding.uploadedDocuments.domain.model.UploadedDocs;
import com.onboarding.uploadedDocuments.domain.outport.UploadedDocsRepositoryPort;
import com.onboarding.uploadedDocuments.mapper.UploadedDocsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UploadDocumentCommandHandler implements CommandHandler<UploadDocumentCommand, UploadedDocsDto.Response> {

    private final UploadedDocsRepositoryPort uploadedDocsPort;
    private final DocumentsReqRepository documentsReqPort;
    private final UploadedDocsMapper mapper;

    @Value("${app.storage.path}")
    private String storagePath;

    private static final String FILENAME_FORMAT_REGEX =
            "^\\[.+\\] [A-Za-z\\u00C0-\\u024F]+(?: [A-Za-z\\u00C0-\\u024F]+)?, [A-Za-z\\u00C0-\\u024F]+(?:\\s[A-Za-z\\u00C0-\\u024F]+)*$";

    @Override
    public UploadedDocsDto.Response handle(UploadDocumentCommand command) {
        DocumentsReq requirement = documentsReqPort.findById(command.getRequirementId())
                .orElseThrow(() -> new IllegalArgumentException("Requisito de documento no encontrado."));

        validateFile(command.getFile(), requirement);

        try {
            String storedFilename = UUID.randomUUID() + "_" + command.getFile().getOriginalFilename();
            Path destination = Paths.get(storagePath, command.getProcessId());
            Files.createDirectories(destination);
            Files.copy(command.getFile().getInputStream(), destination.resolve(storedFilename));

            UploadedDocs doc = uploadedDocsPort
                    .findByOnboardingProcessIdAndDocumentRequirementId(command.getProcessId(), command.getRequirementId())
                    .orElse(new UploadedDocs());

            doc.setOnboardingProcessId(command.getProcessId());
            doc.setDocumentRequirementId(command.getRequirementId());
            doc.setFilePath(destination.resolve(storedFilename).toString());
            doc.setOriginalFilename(command.getFile().getOriginalFilename());
            doc.setMimeType(command.getFile().getContentType());
            doc.setFileSizeBytes(command.getFile().getSize());
            doc.setValidationStatus(UploadedDocs.ValidationStatus.PENDING_VALIDATION);

            return mapper.toResponse(uploadedDocsPort.save(doc));
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el documento.", e);
        }
    }

    private void validateFile(MultipartFile file, DocumentsReq requirement) {
        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío.");
        }

        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.') + 1).toUpperCase()
                : "";
        List<String> allowed = Arrays.stream(requirement.getAllowedFormats().split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .toList();
        if (!allowed.contains(extension)) {
            throw new IllegalArgumentException("Formato no permitido. Formatos aceptados: " + requirement.getAllowedFormats());
        }

        String nameWithoutExt = originalName.contains(".")
                ? originalName.substring(0, originalName.lastIndexOf('.'))
                : originalName;
        if (!nameWithoutExt.matches(FILENAME_FORMAT_REGEX)) {
            throw new IllegalArgumentException(
                    "El nombre del archivo debe seguir el formato: [Nombre del documento] Apellido Apellido, Nombre");
        }
    }
}
