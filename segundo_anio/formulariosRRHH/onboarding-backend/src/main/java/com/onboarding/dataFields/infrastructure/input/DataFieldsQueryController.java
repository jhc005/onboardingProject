package com.onboarding.dataFields.infrastructure.input;

import com.onboarding.dataFields.application.dto.DataFieldsDto;
import com.onboarding.dataFields.application.queries.CanModifyDataFieldsQuery;
import com.onboarding.dataFields.application.queries.GetAllDataFieldsQuery;
import com.onboarding.shared.cqrs.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataFieldsQueryController {

    private final QueryBus queryBus;

    @GetMapping("/api/admin/fields")
    public ResponseEntity<List<DataFieldsDto.Response>> findAll() {
        return ResponseEntity.ok(queryBus.ask(new GetAllDataFieldsQuery()));
    }

    @GetMapping("/api/candidate/form/fields")
    public ResponseEntity<List<DataFieldsDto.Response>> findAllForCandidate() {
        return ResponseEntity.ok(queryBus.ask(new GetAllDataFieldsQuery()));
    }

    @GetMapping("/api/admin/fields/can-modify")
    public ResponseEntity<Boolean> canModify() {
        return ResponseEntity.ok(queryBus.ask(new CanModifyDataFieldsQuery()));
    }
}
