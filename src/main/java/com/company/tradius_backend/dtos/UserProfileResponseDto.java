package com.company.tradius_backend.dtos;


import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserProfileResponseDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profileImageUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
