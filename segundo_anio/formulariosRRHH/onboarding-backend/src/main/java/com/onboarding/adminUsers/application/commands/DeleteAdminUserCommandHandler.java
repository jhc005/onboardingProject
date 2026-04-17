package com.onboarding.adminUsers.application.commands;

import org.springframework.stereotype.Component;

import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.shared.cqrs.CommandHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeleteAdminUserCommandHandler implements CommandHandler<DeleteAdminUserCommand, Void> {

    private final AdminUsersRepositoryPort adminUsersRepositoryPort;

    @Override
    public Void handle(DeleteAdminUserCommand command) {
        adminUsersRepositoryPort.deleteById(command.getId());
        return null;
    };
}
