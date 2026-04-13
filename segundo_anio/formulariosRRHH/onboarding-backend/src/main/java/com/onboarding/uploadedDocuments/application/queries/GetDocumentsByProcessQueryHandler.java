package com.onboarding.uploadedDocuments.application.queries;

import com.onboarding.shared.cqrs.QueryHandler;
import com.onboarding.uploadedDocuments.application.dto.UploadedDocsDto;
import com.onboarding.uploadedDocuments.domain.outport.UploadedDocsRepositoryPort;
import com.onboarding.uploadedDocuments.mapper.UploadedDocsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetDocumentsByProcessQueryHandler implements QueryHandler<GetDocumentsByProcessQuery, List<UploadedDocsDto.Response>> {

    private final UploadedDocsRepositoryPort uploadedDocsPort;
    private final UploadedDocsMapper mapper;

    @Override
    public List<UploadedDocsDto.Response> handle(GetDocumentsByProcessQuery query) {
        return uploadedDocsPort.findByOnboardingProcessId(query.getProcessId()).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
