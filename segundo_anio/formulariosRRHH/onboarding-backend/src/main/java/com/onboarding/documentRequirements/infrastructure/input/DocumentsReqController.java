package com.onboarding.documentRequirements.infrastructure.input;

import com.onboarding.documentRequirements.application.DocumentsReqService;
import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
