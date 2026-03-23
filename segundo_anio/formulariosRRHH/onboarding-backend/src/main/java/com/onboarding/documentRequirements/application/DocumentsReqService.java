package com.onboarding.documentRequirements.application;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.documentRequirements.domain.model.DocumentsReq;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.documentRequirements.mapper.DocumentsReqMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentsReqService {

    private final DocumentsReqRepository documentsReqPort;
    private final DocumentsReqMapper mapper;

    @Value("${app.storage.path}")
    private String storagePath;

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

    public DocumentsReqDto.Response uploadTemplate(Long id, MultipartFile file) throws IOException {
        DocumentsReq req = documentsReqPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado."));

        String storedFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dest = Paths.get(storagePath, "templates");
        Files.createDirectories(dest);
        Files.copy(file.getInputStream(), dest.resolve(storedFilename));

        req.setTemplateFilePath(dest.resolve(storedFilename).toString());
        req.setTemplateOriginalFilename(file.getOriginalFilename());

        return mapper.toResponse(documentsReqPort.save(req));
    }

    public Resource downloadTemplate(Long id) throws IOException {
        DocumentsReq req = documentsReqPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado."));

        if (req.getTemplateFilePath() == null) {
            throw new IllegalArgumentException("Este requisito no tiene plantilla.");
        }

        Path filePath = Paths.get(req.getTemplateFilePath());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new IllegalArgumentException("Archivo de plantilla no encontrado en disco.");
        }

        return resource;
    }

    public String getTemplateOriginalFilename(Long id) {
        return documentsReqPort.findById(id)
                .map(DocumentsReq::getTemplateOriginalFilename)
                .orElseThrow(() -> new IllegalArgumentException("Requisito no encontrado."));
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
