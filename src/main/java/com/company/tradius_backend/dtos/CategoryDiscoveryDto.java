package com.company.tradius_backend.dtos;


import java.util.UUID;

public record CategoryDiscoveryDto(
        UUID categoryId,
        String name
) {}
