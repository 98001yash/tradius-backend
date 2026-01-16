package com.company.tradius_backend.dtos;

import java.util.UUID;

public record ServiceDiscoveryDto(
        UUID serviceId,
        String serviceName,
        Double price,
        Integer durationMinutes,
        String vendorName
) {}
