package com.onboarding.adminUsers.domain.outport;

import com.onboarding.adminUsers.domain.model.AdminUsers;

import java.util.List;
import java.util.Optional;

public interface AdminUsersRepositoryPort {

    AdminUsers save(AdminUsers adminUser);
    Optional<AdminUsers> findById(Long id);
    Optional<AdminUsers> findByEmail(String email);
    Optional<AdminUsers>findByResetToken(String resetToken);
    List<AdminUsers> findAll();
    void deleteById(Long id);
    boolean existsByEmail(String email);
}
