package com.company.tradius_backend.dtos;


import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ServiceOfferingResponseDto {

    private UUID serviceId;

    private String name;
    private String description;
    private Double price;
    private Integer durationMinutes;

    private boolean active;

    private UUID vendorId;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
