package com.company.tradius_backend.dtos;

import java.util.UUID;

public record VendorDiscoveryDto(
        UUID vendorId,
        String businessName,
        String categoryName,
        String areaName,
        String cityName
) {}