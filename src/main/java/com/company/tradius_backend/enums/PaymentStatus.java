package com.company.tradius_backend.enums;

public enum PaymentStatus {

    CREATED, // payment record created locally
    ORDER_CREATED,  // RazorPay order created
    AUTHORIZED,      // payment authorized by the RazorPay
    CAPTURED,   // Payment captured(success)
    FAILED,  // Payment failed
    REFUNDED    // Payment refunded
}
