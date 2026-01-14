package com.company.tradius_backend.service;

import com.company.tradius_backend.dtos.CreatePaymentResponseDto;
import com.razorpay.RazorpayException;

import java.util.UUID;

public interface PaymentService {

    CreatePaymentResponseDto createRazorpayOrder(UUID bookingId) throws RazorpayException;
}
