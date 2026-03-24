package com.onboarding.adminUsers.application;

import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserSeeder implements CommandLineRunner {

    private final AdminUsersRepositoryPort adminUsersPort;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.full-name}")
    private String adminFullName;

    @Override
    public void run(String... args) {
        if (adminUsersPort.existsByEmail(adminEmail)) {
            log.info("Admin user already exists, skipping seed.");
            return;
        }

        AdminUsers admin = new AdminUsers();
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setFullName(adminFullName);
        adminUsersPort.save(admin);

        log.info("Admin user created: {}", adminEmail);
    }
}
