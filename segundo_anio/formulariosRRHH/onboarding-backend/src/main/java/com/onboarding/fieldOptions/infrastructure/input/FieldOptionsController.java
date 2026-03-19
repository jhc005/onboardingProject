package com.onboarding.fieldOptions.infrastructure.input;

import com.onboarding.fieldOptions.application.FieldOptionsService;
import com.onboarding.fieldOptions.application.dto.FieldOptionsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/fields/{fieldId}/options")
@RequiredArgsConstructor
public class FieldOptionsController {

    private final FieldOptionsService fieldOptionsService;

    @GetMapping
    public ResponseEntity<List<FieldOptionsDto.Response>> findByField(@PathVariable Long fieldId) {
        return ResponseEntity.ok(fieldOptionsService.findByDataFieldId(fieldId));
    }

    @PostMapping
    public ResponseEntity<FieldOptionsDto.Response> create(@PathVariable Long fieldId,
                                                           @RequestBody FieldOptionsDto.Request request) {
        request.setDataFieldId(fieldId);
        return ResponseEntity.status(HttpStatus.CREATED).body(fieldOptionsService.create(request));
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> delete(@PathVariable Long fieldId, @PathVariable Long optionId) {
        fieldOptionsService.delete(optionId);
        return ResponseEntity.noContent().build();
    }
}
