package com.onboarding.accessTokens.infrastructure.input;

import com.onboarding.accessTokens.application.commands.ValidateTokenCommand;
import com.onboarding.accessTokens.application.dto.AcessTokensDto;
import com.onboarding.shared.cqrs.CommandBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AccessTokensCommandController {

    private final CommandBus commandBus;

    @PostMapping("/validate-token")
    public ResponseEntity<AcessTokensDto.ValidateResponse> validateToken(
            @RequestBody AcessTokensDto.ValidateRequest request) {
        return ResponseEntity.ok(commandBus.dispatch(new ValidateTokenCommand(request.getToken())));
    }
}
