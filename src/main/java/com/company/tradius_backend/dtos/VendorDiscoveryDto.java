package com.company.tradius_backend.dtos;

import java.util.UUID;

public record VendorDiscoveryDto(
        UUID vendorId,
        String businessName,
        String categoryName,
        UUID id, String areaName,
        UUID cityId, String cityName
) {}