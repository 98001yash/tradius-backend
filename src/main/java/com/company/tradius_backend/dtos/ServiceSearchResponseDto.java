package com.company.tradius_backend.dtos;

import java.util.UUID;

public record ServiceSearchResponseDto(
        UUID serviceId,
        String serviceName,
        Double price,
        Integer durationMinutes,
        UUID vendorId,
        String vendorName,
        String city,
        String area
) {}