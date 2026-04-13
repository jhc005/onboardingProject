package com.onboarding.documentRequirements.infrastructure.input;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.documentRequirements.application.queries.CanModifyDocumentRequirementsQuery;
import com.onboarding.documentRequirements.application.queries.DownloadTemplateQuery;
import com.onboarding.documentRequirements.application.queries.GetAllDocumentRequirementsQuery;
import com.onboarding.shared.cqrs.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentsReqQueryController {

    private final QueryBus queryBus;

    @GetMapping("/api/admin/document-requirements")
    public ResponseEntity<List<DocumentsReqDto.Response>> findAll() {
        return ResponseEntity.ok(queryBus.ask(new GetAllDocumentRequirementsQuery()));
    }

    @GetMapping("/api/candidate/form/document-requirements")
    public ResponseEntity<List<DocumentsReqDto.Response>> findAllForCandidate() {
        return ResponseEntity.ok(queryBus.ask(new GetAllDocumentRequirementsQuery()));
    }

    @GetMapping("/api/admin/document-requirements/can-modify")
    public ResponseEntity<Boolean> canModify() {
        return ResponseEntity.ok(queryBus.ask(new CanModifyDocumentRequirementsQuery()));
    }

    @GetMapping("/api/admin/document-requirements/{id}/template/download")
    public ResponseEntity<Resource> downloadTemplateAdmin(@PathVariable Long id) {
        return buildDownloadResponse(id);
    }

    @GetMapping("/api/candidate/form/document-requirements/{id}/template/download")
    public ResponseEntity<Resource> downloadTemplateCandidate(@PathVariable Long id) {
        return buildDownloadResponse(id);
    }

    private ResponseEntity<Resource> buildDownloadResponse(Long id) {
        DownloadTemplateQuery.Result result = queryBus.ask(new DownloadTemplateQuery(id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.filename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(result.resource());
    }
}
