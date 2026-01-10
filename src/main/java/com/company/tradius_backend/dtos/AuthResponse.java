package com.company.tradius_backend.dtos;

import com.company.tradius_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    private UUID userId;
    private String email;
    private Role role;
    private String accessToken;
}
