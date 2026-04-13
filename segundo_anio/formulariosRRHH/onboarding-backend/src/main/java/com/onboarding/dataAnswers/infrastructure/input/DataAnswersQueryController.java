package com.onboarding.dataAnswers.infrastructure.input;

import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import com.onboarding.dataAnswers.application.queries.GetAnswersByProcessQuery;
import com.onboarding.shared.cqrs.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataAnswersQueryController {

    private final QueryBus queryBus;

    @GetMapping("/api/candidate/form/{processId}/answers")
    public ResponseEntity<List<DataAnswersDto.Response>> findByProcess(@PathVariable String processId) {
        return ResponseEntity.ok(queryBus.ask(new GetAnswersByProcessQuery(processId)));
    }

    @GetMapping("/api/admin/processes/{processId}/answers")
    public ResponseEntity<List<DataAnswersDto.Response>> findByProcessAdmin(@PathVariable String processId) {
        return ResponseEntity.ok(queryBus.ask(new GetAnswersByProcessQuery(processId)));
    }
}
