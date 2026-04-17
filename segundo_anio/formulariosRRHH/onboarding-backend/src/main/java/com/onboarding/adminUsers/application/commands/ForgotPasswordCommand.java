package com.onboarding.adminUsers.application.commands;

import com.onboarding.shared.cqrs.Command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ForgotPasswordCommand implements Command {
    private final String email;
}
