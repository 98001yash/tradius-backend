package com.company.tradius_backend.controller;


import com.company.tradius_backend.dtos.CreatePaymentResponseDto;
import com.company.tradius_backend.service.PaymentService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    public CreatePaymentResponseDto createRazorpayOrder(
            @PathVariable UUID bookingId
    ) throws RazorpayException {
        return paymentService.createRazorpayOrder(bookingId);
    }
}
