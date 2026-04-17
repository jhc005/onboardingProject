package com.onboarding.adminUsers.application.commands;

import com.onboarding.shared.cqrs.Command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResetPasswordCommand implements Command {
    private final String token;
    private final String newPassword;
}
