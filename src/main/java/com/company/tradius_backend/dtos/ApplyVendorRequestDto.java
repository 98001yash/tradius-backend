package com.company.tradius_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ApplyVendorRequestDto {

    @NotBlank(message = "Business name is required")
    private String businessName;
    private String description;

    @NotNull(message = "Category is required")
    private UUID categoryId;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotBlank(message = "city is required")
    private String city;
    private String area;
    private String addressLine;

    @NotNull(message = "service radius is required")
    private Integer serviceRadiusKm;
}
