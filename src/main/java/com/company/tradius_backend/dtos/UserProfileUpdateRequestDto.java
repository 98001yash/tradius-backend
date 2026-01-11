package com.company.tradius_backend.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdateRequestDto {

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 100, message = "first name too long")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 100, message = "last name too long")
    private String lastName;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be 10 digits"
    )
    private String phoneNumber;

    private String profileImageUrl;
}
