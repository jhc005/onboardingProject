package com.onboarding.adminUsers.application;

import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.adminUsers.mapper.AdminUsersMapper;
import com.onboarding.config.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUsersService {

    private final AdminUsersRepositoryPort adminUsersPort;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminUsersMapper mapper;
    private final JwtService jwtService;

    public AdminUsersDto.Response create(AdminUsersDto.CreateRequest request) {
        if (adminUsersPort.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Ya existe un administrador con ese email.");
        }
        AdminUsers admin = new AdminUsers();
        admin.setEmail(request.getEmail());
        admin.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        admin.setFullName(request.getFullName());
        AdminUsers saved = adminUsersPort.save(admin);
        return mapper.toResponse(saved);
    }

    public AdminUsersDto.LoginResponse login(AdminUsersDto.LoginRequest request) {
        AdminUsers admin = adminUsersPort.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales incorrectas."));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
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

    public List<AdminUsersDto.Response> findAll() {
        return adminUsersPort.findAll().stream().map(mapper::toResponse).toList();
    }
}
