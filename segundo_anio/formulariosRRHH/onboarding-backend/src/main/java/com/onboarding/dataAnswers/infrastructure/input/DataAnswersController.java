package com.onboarding.dataAnswers.infrastructure.input;

import com.onboarding.dataAnswers.application.DataAnswersService;
import com.onboarding.dataAnswers.application.dto.DataAnswersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataAnswersController {

    private final DataAnswersService dataAnswersService;
    @GetMapping("/api/candidate/form/{processId}/answers")
    public ResponseEntity<List<DataAnswersDto.Response>> findByProcess(@PathVariable String processId) {
        return ResponseEntity.ok(dataAnswersService.findByProcessId(processId));
    }

    @GetMapping("/api/admin/processes/{processId}/answers")
    public ResponseEntity<List<DataAnswersDto.Response>> findByProcessAdmin(@PathVariable String processId) {
        return ResponseEntity.ok(dataAnswersService.findByProcessId(processId));
    }
    
    @PostMapping("/api/candidate/form/{processId}/answers")
    public ResponseEntity<List<DataAnswersDto.Response>> saveAnswers(
            @PathVariable String processId,
            @RequestBody DataAnswersDto.SaveRequest request) {
        return ResponseEntity.ok(dataAnswersService.saveAnswers(processId, request));
    }

    
}
