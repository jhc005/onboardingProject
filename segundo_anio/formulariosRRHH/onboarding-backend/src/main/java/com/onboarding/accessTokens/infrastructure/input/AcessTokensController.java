package com.onboarding.accessTokens.infrastructure.input;

import com.onboarding.accessTokens.application.AcessTokensService;
import com.onboarding.accessTokens.application.dto.AcessTokensDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AcessTokensController {

    private final AcessTokensService tokenService;

    @PostMapping("/validate-token")
    public ResponseEntity<AcessTokensDto.ValidateResponse> validateToken(
            @RequestBody AcessTokensDto.ValidateRequest request) {
        return ResponseEntity.ok(tokenService.validateToken(request.getToken()));
    }
}
