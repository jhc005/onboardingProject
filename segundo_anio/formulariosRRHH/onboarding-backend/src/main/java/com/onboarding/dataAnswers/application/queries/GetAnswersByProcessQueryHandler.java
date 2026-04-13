package com.onboarding.dataAnswers.application.queries;

import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import com.onboarding.dataAnswers.domain.outport.DataAnswersRepositoryPort;
import com.onboarding.dataAnswers.mapper.DataAnswersMapper;
import com.onboarding.shared.cqrs.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnswersByProcessQueryHandler implements QueryHandler<GetAnswersByProcessQuery, List<DataAnswersDto.Response>> {

    private final DataAnswersRepositoryPort answersPort;
    private final DataAnswersMapper mapper;

    @Override
    public List<DataAnswersDto.Response> handle(GetAnswersByProcessQuery query) {
        return answersPort.findByOnboardingProcessId(query.getProcessId()).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
