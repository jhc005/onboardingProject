package com.onboarding.candidates.application.queries;

import com.onboarding.candidates.application.dto.CandidatesDto;
import com.onboarding.candidates.domain.outport.CandidatesRepositoryPort;
import com.onboarding.candidates.mapper.CandidatesMapper;
import com.onboarding.shared.cqrs.QueryHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllCandidatesQueryHandler implements QueryHandler<GetAllCandidatesQuery, List<CandidatesDto.Response>> {

    private final CandidatesRepositoryPort candidatesPort;
    private final CandidatesMapper mapper;

    @Override
    public List<CandidatesDto.Response> handle(GetAllCandidatesQuery query) {
        return candidatesPort.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }
}
