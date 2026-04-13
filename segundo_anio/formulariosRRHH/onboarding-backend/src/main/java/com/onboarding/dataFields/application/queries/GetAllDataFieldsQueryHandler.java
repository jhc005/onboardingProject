package com.onboarding.dataFields.application.queries;

import com.onboarding.dataFields.application.dto.DataFieldsDto;
import com.onboarding.dataFields.domain.outport.DataFieldsRepositoryPort;
import com.onboarding.dataFields.mapper.DataFieldsMapper;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllDataFieldsQueryHandler implements QueryHandler<GetAllDataFieldsQuery, List<DataFieldsDto.Response>> {

    private final DataFieldsRepositoryPort dataFieldsPort;
    private final DataFieldsMapper mapper;

    @Override
    public List<DataFieldsDto.Response> handle(GetAllDataFieldsQuery query) {
        return dataFieldsPort.findAllOrderedByDisplayOrder().stream()
                .map(mapper::toResponse)
                .toList();
    }
}
