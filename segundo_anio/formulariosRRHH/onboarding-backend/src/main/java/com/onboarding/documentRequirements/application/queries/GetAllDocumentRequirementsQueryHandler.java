package com.onboarding.documentRequirements.application.queries;

import com.onboarding.documentRequirements.application.dto.DocumentsReqDto;
import com.onboarding.documentRequirements.domain.outport.DocumentsReqRepository;
import com.onboarding.documentRequirements.mapper.DocumentsReqMapper;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllDocumentRequirementsQueryHandler implements QueryHandler<GetAllDocumentRequirementsQuery, List<DocumentsReqDto.Response>> {

    private final DocumentsReqRepository documentsReqPort;
    private final DocumentsReqMapper mapper;

    @Override
    public List<DocumentsReqDto.Response> handle(GetAllDocumentRequirementsQuery query) {
        return documentsReqPort.findAllOrderedByDisplayOrder().stream()
                .map(mapper::toResponse)
                .toList();
    }
}
