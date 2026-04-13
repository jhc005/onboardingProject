package com.onboarding.fieldOptions.application.queries;

import com.onboarding.fieldOptions.application.dto.FieldOptionsDto;
import com.onboarding.fieldOptions.domain.outport.FieldOptionsRepositoryPort;
import com.onboarding.fieldOptions.mapper.FieldOptionsMapper;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetFieldOptionsByDataFieldQueryHandler implements QueryHandler<GetFieldOptionsByDataFieldQuery, List<FieldOptionsDto.Response>> {

    private final FieldOptionsRepositoryPort fieldOptionsPort;
    private final FieldOptionsMapper mapper;

    @Override
    public List<FieldOptionsDto.Response> handle(GetFieldOptionsByDataFieldQuery query) {
        return fieldOptionsPort.findByDataFieldId(query.getDataFieldId()).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
