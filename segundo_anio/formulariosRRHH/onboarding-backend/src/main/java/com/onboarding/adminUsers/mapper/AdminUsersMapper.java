package com.onboarding.adminUsers.mapper;

import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.infrastructure.output.jpa.AdminUsersJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AdminUsersMapper {

    public AdminUsers toDomain(AdminUsersJpaEntity entity) {
        AdminUsers domain = new AdminUsers();
        domain.setId(entity.getId());
        domain.setEmail(entity.getEmail());
        domain.setPasswordHash(entity.getPasswordHash());
        domain.setFullName(entity.getFullName());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setResetToken(entity.getResetToken());
        domain.setResetTokenExpiresAt(entity.getResetTokenExpiresAt());
        return domain;
    }

    public AdminUsersJpaEntity toEntity(AdminUsers domain) {
        AdminUsersJpaEntity entity = new AdminUsersJpaEntity();
        entity.setId(domain.getId());
        entity.setEmail(domain.getEmail());
        entity.setPasswordHash(domain.getPasswordHash());
        entity.setFullName(domain.getFullName());
        entity.setResetToken(domain.getResetToken());
        entity.setResetTokenExpiresAt(domain.getResetTokenExpiresAt());
        return entity;
    }

    public AdminUsersDto.Response toResponse(AdminUsers admin) {
        return AdminUsersDto.Response.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .fullName(admin.getFullName())
                .createdAt(admin.getCreatedAt())
                .build();
    }
}
