package com.onboarding.adminUsers.infrastructure.input;

import com.onboarding.adminUsers.application.commands.CreateAdminUserCommand;
import com.onboarding.adminUsers.application.commands.LoginCommand;
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
}
