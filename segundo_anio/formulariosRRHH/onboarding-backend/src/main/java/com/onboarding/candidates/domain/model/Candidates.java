package com.onboarding.candidates.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidates {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
}
