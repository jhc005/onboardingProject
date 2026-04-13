package com.onboarding.fieldOptions.infrastructure.input;

import com.onboarding.fieldOptions.application.commands.CreateFieldOptionCommand;
import com.onboarding.fieldOptions.application.commands.DeleteFieldOptionCommand;
import com.onboarding.fieldOptions.application.dto.FieldOptionsDto;
import com.onboarding.shared.cqrs.CommandBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/fields/{fieldId}/options")
@RequiredArgsConstructor
public class FieldOptionsCommandController {

    private final CommandBus commandBus;

    @PostMapping
    public ResponseEntity<FieldOptionsDto.Response> create(@PathVariable Long fieldId,
                                                           @RequestBody FieldOptionsDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                commandBus.dispatch(new CreateFieldOptionCommand(fieldId, request.getOptionLabel(), request.getOptionValue(), request.getDisplayOrder()))
        );
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> delete(@PathVariable Long fieldId, @PathVariable Long optionId) {
        commandBus.dispatch(new DeleteFieldOptionCommand(optionId));
        return ResponseEntity.noContent().build();
    }
}
