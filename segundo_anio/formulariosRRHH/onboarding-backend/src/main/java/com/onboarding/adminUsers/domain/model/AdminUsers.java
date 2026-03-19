package com.onboarding.adminUsers.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUsers {

    private Long id;
    private String email;
    private String passwordHash;
    private String fullName;
    private LocalDateTime createdAt;
}
