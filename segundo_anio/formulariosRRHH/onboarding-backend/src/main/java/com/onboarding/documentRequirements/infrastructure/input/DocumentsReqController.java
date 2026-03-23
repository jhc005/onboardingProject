package com.onboarding.documentRequirements.infrastructure.input;

import com.onboarding.documentRequirements.application.DocumentsReqService;
import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentsReqController {

    private final DocumentsReqService documentsReqService;

    @GetMapping("/api/admin/document-requirements")
    public ResponseEntity<List<DocumentsReqDto.Response>> findAll() {
        return ResponseEntity.ok(documentsReqService.findAll());
    }

    @PostMapping("/api/admin/document-requirements")
    public ResponseEntity<DocumentsReqDto.Response> create(@RequestBody DocumentsReqDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentsReqService.create(request));
    }

    @PutMapping("/api/admin/document-requirements/{id}")
    public ResponseEntity<DocumentsReqDto.Response> update(@PathVariable Long id,
                                                           @RequestBody DocumentsReqDto.Request request) {
        return ResponseEntity.ok(documentsReqService.update(id, request));
    }

    @DeleteMapping("/api/admin/document-requirements/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentsReqService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/candidate/form/document-requirements")
    public ResponseEntity<List<DocumentsReqDto.Response>> findAllForCandidate() {
        return ResponseEntity.ok(documentsReqService.findAll());
    }

    @PostMapping("/api/admin/document-requirements/{id}/template")
    public ResponseEntity<DocumentsReqDto.Response> uploadTemplate(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(documentsReqService.uploadTemplate(id, file));
    }

    @GetMapping("/api/admin/document-requirements/{id}/template/download")
    public ResponseEntity<Resource> downloadTemplateAdmin(@PathVariable Long id) throws IOException {
        return buildDownloadResponse(id);
    }

    @GetMapping("/api/candidate/form/document-requirements/{id}/template/download")
    public ResponseEntity<Resource> downloadTemplateCandidate(@PathVariable Long id) throws IOException {
        return buildDownloadResponse(id);
    }

    private ResponseEntity<Resource> buildDownloadResponse(Long id) throws IOException {
        Resource resource = documentsReqService.downloadTemplate(id);
        String filename = documentsReqService.getTemplateOriginalFilename(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
