package com.onboarding.uploadedDocuments.infrastructure.input;

import com.onboarding.shared.cqrs.CommandBus;
import com.onboarding.uploadedDocuments.application.commands.UploadDocumentCommand;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UploadedDocsCommandController {

    private final CommandBus commandBus;

    @PostMapping("/api/candidate/form/{processId}/documents/{requirementId}/upload")
    public ResponseEntity<UploadedDocsDto.Response> upload(
            @PathVariable String processId,
            @PathVariable Long requirementId,
            @RequestParam MultipartFile file) {
        return ResponseEntity.ok(commandBus.dispatch(new UploadDocumentCommand(processId, requirementId, file)));
    }
}
