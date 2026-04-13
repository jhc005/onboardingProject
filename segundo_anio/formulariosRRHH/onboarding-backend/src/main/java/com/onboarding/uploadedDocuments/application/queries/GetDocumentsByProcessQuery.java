package com.onboarding.uploadedDocuments.application.queries;

import com.onboarding.shared.cqrs.Query;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetDocumentsByProcessQuery implements Query<List<UploadedDocsDto.Response>> {
    private final String processId;
}
