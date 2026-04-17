package com.onboarding.adminUsers.infrastructure.input;

import com.onboarding.adminUsers.application.commands.CreateAdminUserCommand;
import com.onboarding.adminUsers.application.commands.DeleteAdminUserCommand;
import com.onboarding.adminUsers.application.commands.ForgotPasswordCommand;
import com.onboarding.adminUsers.application.commands.LoginCommand;
import com.onboarding.adminUsers.application.commands.ResetPasswordCommand;
import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.shared.cqrs.CommandBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUsersCommandController {

    private final CommandBus commandBus;

    @PostMapping("/auth/login")
    public ResponseEntity<AdminUsersDto.LoginResponse> login(@RequestBody AdminUsersDto.LoginRequest request) {
        return ResponseEntity.ok(commandBus.dispatch(new LoginCommand(request.getEmail(), request.getPassword())));
    }

    @PostMapping("/users")
    public ResponseEntity<AdminUsersDto.Response> create(@RequestBody AdminUsersDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                commandBus.dispatch(new CreateAdminUserCommand(request.getEmail(), request.getPassword(), request.getFullName()))
        );
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<Void>forgotPassword(@RequestBody AdminUsersDto.ForgotPasswordRequest request) {
        commandBus.dispatch(new ForgotPasswordCommand(request.getEmail()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody AdminUsersDto.ResetPasswordRequest request) {
        commandBus.dispatch(new ResetPasswordCommand(request.getToken(), request.getNewPassword()));
        return ResponseEntity.noContent().build();
    }
    

    @DeleteMapping("users/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        commandBus.dispatch(new DeleteAdminUserCommand(userId));
        return ResponseEntity.noContent().build();
    }    
}
