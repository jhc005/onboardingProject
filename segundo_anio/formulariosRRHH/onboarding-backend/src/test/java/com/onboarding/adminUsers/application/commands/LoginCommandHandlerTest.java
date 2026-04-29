package com.onboarding.adminUsers.application.commands;

import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.adminUsers.domain.model.AdminUsers;
import com.onboarding.adminUsers.domain.outport.AdminUsersRepositoryPort;
import com.onboarding.config.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginCommandHandlerTest {

    @Mock
    private AdminUsersRepositoryPort adminUsersPort;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginCommandHandler handler;

    @Test
    void handle_withValidCredentials_returnsTokenAndUserInfo() {
        AdminUsers admin = new AdminUsers();
        admin.setId(1L);
        admin.setEmail("admin@empresa.com");
        admin.setPasswordHash("$2a$hashed");
        admin.setFullName("Admin Empresa");

        when(adminUsersPort.findByEmail("admin@empresa.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("contraseña123", "$2a$hashed")).thenReturn(true);
        when(jwtService.generateToken("admin@empresa.com")).thenReturn("jwt-token-generado");

        AdminUsersDto.LoginResponse response =
                handler.handle(new LoginCommand("admin@empresa.com", "contraseña123"));

        assertThat(response.getToken()).isEqualTo("jwt-token-generado");
        assertThat(response.getEmail()).isEqualTo("admin@empresa.com");
        assertThat(response.getFullName()).isEqualTo("Admin Empresa");
    }

    @Test
    void handle_withUnknownEmail_throwsWithGenericMessage() {
        when(adminUsersPort.findByEmail("inexistente@empresa.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(new LoginCommand("inexistente@empresa.com", "cualquiera")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credenciales incorrectas.");
    }

    @Test
    void handle_withWrongPassword_throwsWithGenericMessage() {
        AdminUsers admin = new AdminUsers();
        admin.setEmail("admin@empresa.com");
        admin.setPasswordHash("$2a$hashed");

        when(adminUsersPort.findByEmail("admin@empresa.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("contraseñaIncorrecta", "$2a$hashed")).thenReturn(false);

        assertThatThrownBy(() -> handler.handle(new LoginCommand("admin@empresa.com", "contraseñaIncorrecta")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Credenciales incorrectas.");
    }
}
