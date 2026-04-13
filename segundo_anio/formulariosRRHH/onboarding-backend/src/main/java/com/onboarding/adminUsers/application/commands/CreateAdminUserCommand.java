package com.onboarding.adminUsers.application.commands;

import com.onboarding.shared.cqrs.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateAdminUserCommand implements Command {
    private final String email;
    private final String password;
    private final String fullName;
}
