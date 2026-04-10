package com.onboarding.dataFields.infrastructure.input;

import com.onboarding.dataFields.application.DataFieldsService;
import com.onboarding.dataFields.application.dto.DataFieldsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class DataFieldsController {

    private final DataFieldsService dataFieldsService;

    @GetMapping("/api/admin/fields")
    public ResponseEntity<List<DataFieldsDto.Response>> findAll() {
        return ResponseEntity.ok(dataFieldsService.findAll());
    }

    @GetMapping("/api/candidate/form/fields")
    public ResponseEntity<List<DataFieldsDto.Response>> findAllForCandidate() {
        return ResponseEntity.ok(dataFieldsService.findAll());
    }

    @GetMapping("/api/admin/fields/can-modify")
    public ResponseEntity<Boolean> canModify() {
        return ResponseEntity.ok(dataFieldsService.canModify());
    }
    

    @PostMapping("/api/admin/fields")
    public ResponseEntity<DataFieldsDto.Response> create(@RequestBody DataFieldsDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(dataFieldsService.create(request));
    }

    @PutMapping("/api/admin/fields/{id}")
    public ResponseEntity<DataFieldsDto.Response> update(@PathVariable Long id,
                                                         @RequestBody DataFieldsDto.Request request) {
        return ResponseEntity.ok(dataFieldsService.update(id, request));
    }

    @DeleteMapping("/api/admin/fields/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dataFieldsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
