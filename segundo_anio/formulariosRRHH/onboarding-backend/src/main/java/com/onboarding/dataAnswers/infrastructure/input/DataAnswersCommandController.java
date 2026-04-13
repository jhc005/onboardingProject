package com.onboarding.dataAnswers.infrastructure.input;

import com.onboarding.dataAnswers.application.commands.SaveAnswersCommand;
import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import com.onboarding.shared.cqrs.CommandBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataAnswersCommandController {

    private final CommandBus commandBus;

    @PostMapping("/api/candidate/form/{processId}/answers")
    public ResponseEntity<List<DataAnswersDto.Response>> saveAnswers(
            @PathVariable String processId,
            @RequestBody DataAnswersDto.SaveRequest request) {
        return ResponseEntity.ok(commandBus.dispatch(new SaveAnswersCommand(processId, request)));
    }
}
