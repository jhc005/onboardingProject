package com.onboarding.dataAnswers.application.commands;

import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import com.onboarding.dataAnswers.domain.model.DataAnswers;
import com.onboarding.dataAnswers.domain.outport.DataAnswersRepositoryPort;
import com.onboarding.dataAnswers.mapper.DataAnswersMapper;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveAnswersCommandHandler implements CommandHandler<SaveAnswersCommand, List<DataAnswersDto.Response>> {

    private final DataAnswersRepositoryPort answersPort;
    private final DataAnswersMapper mapper;

    @Override
    @Transactional
    public List<DataAnswersDto.Response> handle(SaveAnswersCommand command) {
        return command.getRequest().getAnswers().stream().map(item -> {
            DataAnswers existing = answersPort
                    .findByOnboardingProcessIdAndDataFieldId(command.getProcessId(), item.getDataFieldId())
                    .orElse(new DataAnswers());

            existing.setOnboardingProcessId(command.getProcessId());
            existing.setDataFieldId(item.getDataFieldId());
            existing.setAnswerValue(item.getAnswerValue());

            return mapper.toResponse(answersPort.save(existing));
        }).toList();
    }
}
