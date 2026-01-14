package com.company.tradius_backend.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePaymentResponseDto {

    private String razorpayOrderId;
    private Long amount;
    private String currency;
}
