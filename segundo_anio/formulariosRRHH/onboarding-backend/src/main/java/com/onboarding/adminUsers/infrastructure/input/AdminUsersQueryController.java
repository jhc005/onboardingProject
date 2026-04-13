package com.onboarding.adminUsers.infrastructure.input;

import com.onboarding.adminUsers.application.dto.AdminUsersDto;
import com.onboarding.adminUsers.application.queries.GetAllAdminUsersQuery;
import com.onboarding.shared.cqrs.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUsersQueryController {

    private final QueryBus queryBus;

    @GetMapping("/users")
    public ResponseEntity<List<AdminUsersDto.Response>> findAll() {
        return ResponseEntity.ok(queryBus.ask(new GetAllAdminUsersQuery()));
    }
}
