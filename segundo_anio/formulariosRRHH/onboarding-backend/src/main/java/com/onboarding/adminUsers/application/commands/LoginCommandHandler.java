package com.onboarding.adminUsers.application.commands;

import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.config.JwtService;
import com.onboarding.shared.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginCommandHandler implements CommandHandler<LoginCommand, AdminUsersDto.LoginResponse> {

    private final AdminUsersRepositoryPort adminUsersPort;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AdminUsersDto.LoginResponse handle(LoginCommand command) {
        AdminUsers admin = adminUsersPort.findByEmail(command.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas."));

        if (!passwordEncoder.matches(command.getPassword(), admin.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales incorrectas.");
        }

        String token = jwtService.generateToken(admin.getEmail());

        return AdminUsersDto.LoginResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .fullName(admin.getFullName())
                .token(token)
                .build();
    }
}
