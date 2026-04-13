package com.onboarding.adminUsers.application.commands;

import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.adminUsers.mapper.AdminUsersMapper;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAdminUserCommandHandler implements CommandHandler<CreateAdminUserCommand, AdminUsersDto.Response> {

    private final AdminUsersRepositoryPort adminUsersPort;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminUsersMapper mapper;

    @Override
    public AdminUsersDto.Response handle(CreateAdminUserCommand command) {
        if (adminUsersPort.existsByEmail(command.getEmail())) {
            throw new IllegalArgumentException("Ya existe un administrador con ese email.");
        }
        AdminUsers admin = new AdminUsers();
        admin.setEmail(command.getEmail());
        admin.setPasswordHash(passwordEncoder.encode(command.getPassword()));
        admin.setFullName(command.getFullName());
        return mapper.toResponse(adminUsersPort.save(admin));
    }
}
