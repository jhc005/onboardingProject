package com.onboarding.dataAnswers.application.commands;

import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveAnswersCommand implements Command {
    private final String processId;
    private final DataAnswersDto.SaveRequest request;
}
