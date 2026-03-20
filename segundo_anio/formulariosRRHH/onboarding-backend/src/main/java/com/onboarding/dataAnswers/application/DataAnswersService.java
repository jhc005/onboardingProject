package com.onboarding.dataAnswers.application;

import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import com.onboarding.dataAnswers.domain.model.DataAnswers;
import com.onboarding.dataAnswers.domain.outport.DataAnswersRepositoryPort;
import com.onboarding.dataAnswers.mapper.DataAnswersMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataAnswersService {

    private final DataAnswersRepositoryPort answersPort;
    private final DataAnswersMapper mapper;

    @Transactional
    public List<DataAnswersDto.Response> saveAnswers(String processId, 
        DataAnswersDto.SaveRequest request) {
        return request.getAnswers().stream().map(item -> {
            DataAnswers existing = answersPort
                    .findByOnboardingProcessIdAndDataFieldId(processId, item.getDataFieldId())
                    .orElse(new DataAnswers());

            existing.setOnboardingProcessId(processId);
            existing.setDataFieldId(item.getDataFieldId());
            existing.setAnswerValue(item.getAnswerValue());
            existing.setFieldOptionId(item.getFieldOptionId());

            return mapper.toResponse(answersPort.save(existing));
        }).toList();
    }

    public List<DataAnswersDto.Response> findByProcessId(String processId) {
        return answersPort.findByOnboardingProcessId(processId).stream()
                .map(mapper::toResponse).toList();
    }
}
