package com.onboarding.adminUsers.infrastructure.input;

import com.onboarding.adminUsers.application.AdminUsersService;
import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @PostMapping("/auth/login")
    public ResponseEntity<AdminUsersDto.LoginResponse> login(@RequestBody AdminUsersDto.LoginRequest request) {
        return ResponseEntity.ok(adminUsersService.login(request));
    }

    @PostMapping("/users")
    public ResponseEntity<AdminUsersDto.Response> create(@RequestBody AdminUsersDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUsersService.create(request));
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUsersDto.Response>> findAll() {
        return ResponseEntity.ok(adminUsersService.findAll());
    }
}
