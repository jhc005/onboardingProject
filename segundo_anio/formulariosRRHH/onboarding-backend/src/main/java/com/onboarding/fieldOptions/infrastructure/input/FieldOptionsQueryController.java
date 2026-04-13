package com.onboarding.fieldOptions.infrastructure.input;

import com.onboarding.fieldOptions.application.dto.FieldOptionsDto;
import com.onboarding.fieldOptions.application.queries.GetFieldOptionsByDataFieldQuery;
import com.onboarding.shared.cqrs.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/fields/{fieldId}/options")
@RequiredArgsConstructor
public class FieldOptionsQueryController {

    private final QueryBus queryBus;

    @GetMapping
    public ResponseEntity<List<FieldOptionsDto.Response>> findByField(@PathVariable Long fieldId) {
        return ResponseEntity.ok(queryBus.ask(new GetFieldOptionsByDataFieldQuery(fieldId)));
    }
}
