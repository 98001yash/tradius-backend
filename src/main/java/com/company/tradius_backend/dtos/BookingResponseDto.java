package com.company.tradius_backend.dtos;


import com.company.tradius_backend.enums.BookingStatus;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class BookingResponseDto {

    private UUID bookingId;

    // relations
    private UUID userId;
    private UUID vendorId;
    private UUID serviceId;

    // service info
    private String serviceName;
    private Double servicePrice;

    // Booking details
    private OffsetDateTime scheduledAt;
    private BookingStatus status;
    private String notes;

    // Audit
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
