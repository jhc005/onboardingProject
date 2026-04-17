package com.onboarding.adminUsers.application.commands;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.shared.cqrs.CommandHandler;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ResetPasswordCommandHandler implements CommandHandler<ResetPasswordCommand, Void>{
    private final AdminUsersRepositoryPort adminUsersPort;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Void handle(ResetPasswordCommand command){
        AdminUsers admin = adminUsersPort.findByResetToken(command.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (admin.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("El token ha expirado");
        }

        admin.setPasswordHash(passwordEncoder.encode(command.getNewPassword()));
        admin.setResetToken(null);
        admin.setResetTokenExpiresAt(null);
        adminUsersPort.save(admin);
        return null;
    }
    
}
