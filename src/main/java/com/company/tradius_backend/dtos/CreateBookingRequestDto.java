package com.company.tradius_backend.dtos;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class CreateBookingRequestDto {

    @NotNull(message = "service ID is required")
    private UUID serviceId;

    @NotNull(message = "scheduled time is required")
    @Future(message = "scheduled time must be in the future")
    private OffsetDateTime scheduledAt;

    private String notes;
}
