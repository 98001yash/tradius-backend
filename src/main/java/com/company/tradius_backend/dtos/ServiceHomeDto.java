package com.company.tradius_backend.dtos;

import java.util.UUID;

public record ServiceHomeDto(
        UUID serviceId,
        String name,
        Double price
) {
}
