package com.onboarding.uploadedDocuments.application.queries;

import com.onboarding.shared.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DownloadDocumentQuery implements Query<DownloadDocumentQuery.Result> {

    private final Long docId;

    public record Result(org.springframework.core.io.Resource resource, String filename) {}
}
