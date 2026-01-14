package com.company.tradius_backend.service;

import com.company.tradius_backend.dtos.CreatePaymentResponseDto;

import java.util.UUID;

public interface PaymentService {

    CreatePaymentResponseDto createRazorpayOrder(UUID bookingId);
}
