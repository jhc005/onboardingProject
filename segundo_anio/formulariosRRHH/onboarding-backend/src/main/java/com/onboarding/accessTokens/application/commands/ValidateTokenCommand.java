package com.onboarding.accessTokens.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateTokenCommand implements Command {
    private final String rawToken;
}
