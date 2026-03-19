package com.onboarding.adminUsers.infrastructure.security;

import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final AdminUsersRepositoryPort adminUsersPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminUsers admin = adminUsersPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin no encontrado: " + email));

        return new User(
                admin.getEmail(),
                admin.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
