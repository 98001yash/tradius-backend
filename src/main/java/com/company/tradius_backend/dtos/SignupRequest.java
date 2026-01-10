package com.company.tradius_backend.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    @Email(message = "Invalid email found")
    @NotBlank(message = "Email is required")
    public String email;

    @NotBlank(message = "password is required")
    @Size(min=8, message = "Password must be at least 8 characters")
    private String password;
}
