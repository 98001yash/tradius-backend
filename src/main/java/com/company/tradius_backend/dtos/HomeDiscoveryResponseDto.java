package com.company.tradius_backend.dtos;

import com.company.tradius_backend.entities.Vendor;

import java.util.List;

public record HomeDiscoveryResponseDto (
        AreaInfoDto area,
        List<CategoryDiscoveryDto> categories,
        List<VendorHomeDto> vendors,
        List<ServiceHomeDto> services
){}
