package com.onboarding.dataFields.infrastructure.input;

import com.onboarding.dataFields.application.commands.CreateDataFieldCommand;
import com.onboarding.dataFields.application.commands.DeleteDataFieldCommand;
import com.onboarding.dataFields.application.commands.UpdateDataFieldCommand;
import com.onboarding.dataFields.application.dto.DataFieldsDto;
import com.onboarding.shared.cqrs.CommandBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/fields")
@RequiredArgsConstructor
public class DataFieldsCommandController {

    private final CommandBus commandBus;

    @PostMapping
    public ResponseEntity<DataFieldsDto.Response> create(@RequestBody DataFieldsDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandBus.dispatch(new CreateDataFieldCommand(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataFieldsDto.Response> update(@PathVariable Long id,
                                                         @RequestBody DataFieldsDto.Request request) {
        return ResponseEntity.ok(commandBus.dispatch(new UpdateDataFieldCommand(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commandBus.dispatch(new DeleteDataFieldCommand(id));
        return ResponseEntity.noContent().build();
    }
}
