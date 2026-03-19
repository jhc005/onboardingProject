package com.onboarding.uploadedDocuments.infrastructure.input;

import com.onboarding.uploadedDocuments.application.UploadedDocsService;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UploadedDocsController {

    private final UploadedDocsService uploadedDocsService;

    @PostMapping("/api/candidate/form/{processId}/documents/{requirementId}/upload")
    public ResponseEntity<UploadedDocsDto.Response> upload(
            @PathVariable String processId,
            @PathVariable Long requirementId,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(uploadedDocsService.upload(processId, requirementId, file));
    }

    @GetMapping("/api/candidate/form/{processId}/documents")
    public ResponseEntity<List<UploadedDocsDto.Response>> findByProcess(@PathVariable String processId) {
        return ResponseEntity.ok(uploadedDocsService.findByProcessId(processId));
    }

    @GetMapping("/api/admin/processes/{processId}/documents")
    public ResponseEntity<List<UploadedDocsDto.Response>> findByProcessAdmin(@PathVariable String processId) {
        return ResponseEntity.ok(uploadedDocsService.findByProcessId(processId));
    }

    @PatchMapping("/api/admin/documents/{docId}/approve")
    public ResponseEntity<UploadedDocsDto.Response> approve(@PathVariable Long docId) {
        return ResponseEntity.ok(uploadedDocsService.approveDocument(docId));
    }

    @PatchMapping("/api/admin/documents/{docId}/reject")
    public ResponseEntity<UploadedDocsDto.Response> reject(@PathVariable Long docId,
                                                           @RequestBody UploadedDocsDto.ReviewRequest request) {
        return ResponseEntity.ok(uploadedDocsService.rejectDocument(docId, request.getNotes()));
    }
}
