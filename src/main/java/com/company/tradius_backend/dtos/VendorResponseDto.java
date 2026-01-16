package com.company.tradius_backend.dtos;


import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class VendorResponseDto {

    private UUID vendorId;

    private String businessName;
    private String description;

    private UUID categoryId;
    private String categoryName;

    private Double latitude;
    private Double longitude;

    private UUID cityId;
    private String cityName;

    private UUID areaId;
    private String areaName;

    private String addressLine;
    private Integer serviceRadiusKm;

    private String status;
    private boolean active;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
