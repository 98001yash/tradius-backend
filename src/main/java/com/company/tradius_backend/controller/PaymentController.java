package com.company.tradius_backend.controller;


import com.company.tradius_backend.dtos.CreatePaymentResponseDto;
import com.company.tradius_backend.dtos.VerifyPaymentRequestDto;
import com.company.tradius_backend.service.PaymentService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/razorpay/{bookingId}")
    public CreatePaymentResponseDto createRazorpayOrder(
            @PathVariable UUID bookingId
    ) throws RazorpayException {
        return paymentService.createRazorpayOrder(bookingId);
    }


    @PostMapping("/razorpay/verify")
    public void verifyPayment(
            @Valid @RequestBody VerifyPaymentRequestDto request
    ) throws RazorpayException {
        paymentService.verifyRazorpayPayment(request);
    }

}
