package com.company.tradius_backend.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateServiceOfferingRequestDto {

    @NotBlank(message = "service name is required")
    private String name;
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @Positive(message = "Duration must be greater than zero")
    private Integer durationInMinutes;
}
