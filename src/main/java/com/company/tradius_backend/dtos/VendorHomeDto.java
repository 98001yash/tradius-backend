package com.company.tradius_backend.dtos;

import java.util.UUID;

public record VendorHomeDto(
        UUID vendorId,
        String businessName,
        String category
){}
