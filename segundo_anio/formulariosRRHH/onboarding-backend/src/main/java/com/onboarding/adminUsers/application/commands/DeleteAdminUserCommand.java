package com.onboarding.adminUsers.application.commands;

import com.onboarding.shared.cqrs.Command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteAdminUserCommand implements Command {
    private final Long id;
}
