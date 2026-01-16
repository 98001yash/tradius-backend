package com.company.tradius_backend.dtos;

import java.util.UUID;

public record AreaInfoDto (
        UUID areaId,
        String areaName,
        String cityName
){}
