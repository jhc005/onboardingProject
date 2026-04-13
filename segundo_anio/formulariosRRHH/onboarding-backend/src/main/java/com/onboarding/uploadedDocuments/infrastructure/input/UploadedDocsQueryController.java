package com.onboarding.uploadedDocuments.infrastructure.input;

import com.onboarding.shared.cqrs.QueryBus;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import com.onboarding.uploadedDocuments.application.queries.DownloadDocumentQuery;
import com.onboarding.uploadedDocuments.application.queries.GetDocumentsByProcessQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UploadedDocsQueryController {

    private final QueryBus queryBus;

    @GetMapping("/api/candidate/form/{processId}/documents")
    public ResponseEntity<List<UploadedDocsDto.Response>> findByProcess(@PathVariable String processId) {
        return ResponseEntity.ok(queryBus.ask(new GetDocumentsByProcessQuery(processId)));
    }

    @GetMapping("/api/admin/processes/{processId}/documents")
    public ResponseEntity<List<UploadedDocsDto.Response>> findByProcessAdmin(@PathVariable String processId) {
        return ResponseEntity.ok(queryBus.ask(new GetDocumentsByProcessQuery(processId)));
    }

    @GetMapping("/api/admin/documents/{docId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long docId) {
        DownloadDocumentQuery.Result result = queryBus.ask(new DownloadDocumentQuery(docId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.filename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(result.resource());
    }
}
