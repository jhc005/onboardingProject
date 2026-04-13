package com.onboarding.documentRequirements.infrastructure.input;

import com.onboarding.documentRequirements.application.commands.*;
import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.shared.cqrs.CommandBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class DocumentsReqCommandController {

    private final CommandBus commandBus;

    @PostMapping("/api/admin/document-requirements")
    public ResponseEntity<DocumentsReqDto.Response> create(@RequestBody DocumentsReqDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandBus.dispatch(new CreateDocumentRequirementCommand(request)));
    }

    @PutMapping("/api/admin/document-requirements/{id}")
    public ResponseEntity<DocumentsReqDto.Response> update(@PathVariable Long id,
                                                           @RequestBody DocumentsReqDto.Request request) {
        return ResponseEntity.ok(commandBus.dispatch(new UpdateDocumentRequirementCommand(id, request)));
    }

    @DeleteMapping("/api/admin/document-requirements/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commandBus.dispatch(new DeleteDocumentRequirementCommand(id));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/admin/document-requirements/{id}/template")
    public ResponseEntity<DocumentsReqDto.Response> uploadTemplate(@PathVariable Long id,
                                                                    @RequestParam MultipartFile file) {
        return ResponseEntity.ok(commandBus.dispatch(new UploadTemplateCommand(id, file)));
    }
}
