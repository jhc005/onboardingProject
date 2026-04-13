package com.onboarding.dataAnswers.application.queries;

import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import com.onboarding.shared.cqrs.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetAnswersByProcessQuery implements Query<List<DataAnswersDto.Response>> {
    private final String processId;
}
