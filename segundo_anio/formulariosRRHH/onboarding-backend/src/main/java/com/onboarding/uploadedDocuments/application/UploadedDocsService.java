package com.onboarding.uploadedDocuments.application;

import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import com.onboarding.uploadedDocuments.domain.model.UploadedDocs;
import com.onboarding.uploadedDocuments.domain.outport.UploadedDocsRepositoryPort;
import com.onboarding.uploadedDocuments.mapper.UploadedDocsMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadedDocsService {

    private final UploadedDocsRepositoryPort uploadedDocsPort;
    private final DocumentsReqRepository documentsReqPort;
    private final UploadedDocsMapper mapper;

    @Value("${app.storage.path}")
    private String storagePath;

    public UploadedDocsDto.Response upload(String processId, Long requirementId, MultipartFile file) throws IOException {
        DocumentsReq requirement = documentsReqPort.findById(requirementId)
                .orElseThrow(() -> new IllegalArgumentException("Requisito de documento no encontrado."));

        validateFile(file, requirement);

        String storedFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = Paths.get(storagePath, processId);
        Files.createDirectories(destination);
        Files.copy(file.getInputStream(), destination.resolve(storedFilename));

        UploadedDocs doc = uploadedDocsPort
                .findByOnboardingProcessIdAndDocumentRequirementId(processId, requirementId)
                .orElse(new UploadedDocs());

        doc.setOnboardingProcessId(processId);
        doc.setDocumentRequirementId(requirementId);
        doc.setFilePath(destination.resolve(storedFilename).toString());
        doc.setOriginalFilename(file.getOriginalFilename());
        doc.setMimeType(file.getContentType());
        doc.setFileSizeBytes(file.getSize());
        doc.setValidationStatus(UploadedDocs.ValidationStatus.PENDING_VALIDATION);

        return mapper.toResponse(uploadedDocsPort.save(doc));
    }

    public List<UploadedDocsDto.Response> findByProcessId(String processId) {
        return uploadedDocsPort.findByOnboardingProcessId(processId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public UploadedDocsDto.Response approveDocument(Long docId) {
        UploadedDocs doc = uploadedDocsPort.findById(docId)
                .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado."));

        doc.setValidationStatus(UploadedDocs.ValidationStatus.APPROVED);
        doc.setValidationNotes(null);

        return mapper.toResponse(uploadedDocsPort.save(doc));
    }

    public UploadedDocsDto.Response rejectDocument(Long docId, String notes) {
        UploadedDocs doc = uploadedDocsPort.findById(docId)
                .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado."));

        doc.setValidationStatus(UploadedDocs.ValidationStatus.REJECTED);
        doc.setValidationNotes(notes);

        return mapper.toResponse(uploadedDocsPort.save(doc));
    }

    public Resource downloadDocument(Long docId) throws IOException {
        UploadedDocs doc = uploadedDocsPort.findById(docId)
                .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado."));
        Path path = Paths.get(doc.getFilePath());
        return new UrlResource(path.toUri());
    }

    public String getDocumentOriginalFilename(Long docId) {
        return uploadedDocsPort.findById(docId)
                .orElseThrow(() -> new IllegalArgumentException("Documento no encontrado."))
                .getOriginalFilename();
    }

    private static final String FILENAME_FORMAT_REGEX =
            "^\\[.+\\] [A-Za-z\\u00C0-\\u024F]+(?: [A-Za-z\\u00C0-\\u024F]+)?, [A-Za-z\\u00C0-\\u024F]+(?:\\s[A-Za-z\\u00C0-\\u024F]+)*$";

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
            throw new IllegalArgumentException(
                    "Formato no permitido. Formatos aceptados: " + requirement.getAllowedFormats());
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
