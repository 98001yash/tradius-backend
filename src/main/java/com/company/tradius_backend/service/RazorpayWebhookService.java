package com.company.tradius_backend.service;


import com.company.tradius_backend.entities.Booking;
import com.company.tradius_backend.entities.Payment;
import com.company.tradius_backend.enums.BookingStatus;
import com.company.tradius_backend.enums.PaymentStatus;
import com.company.tradius_backend.repository.PaymentRepository;
import com.company.tradius_backend.utils.RazorpayWebhookVerifier;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RazorpayWebhookService {

    private final PaymentRepository paymentRepository;
    private final RazorpayWebhookVerifier verifier;

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    public void processWebhook(String payload, String signature) {

        if (!verifier.verify(payload, signature, webhookSecret)) {
            log.warn("Invalid Razorpay webhook signature");
            return; // DO NOT THROW
        }

        JSONObject event = new JSONObject(payload);
        String eventType = event.getString("event");

        log.info("Processing webhook event: {}", eventType);

        switch (eventType) {
            case "payment.captured" -> handlePaymentCaptured(event);
            case "payment.failed" -> handlePaymentFailed(event);
            default -> log.info("Unhandled webhook event: {}", eventType);
        }
    }

    private void handlePaymentCaptured(JSONObject event) {

        JSONObject paymentEntity =
                event.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String razorpayOrderId = paymentEntity.getString("order_id");
        String razorpayPaymentId = paymentEntity.getString("id");

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElse(null);

        if (payment == null) {
            log.warn("Payment not found for order {}", razorpayOrderId);
            return;
        }

        if (payment.getStatus() == PaymentStatus.CAPTURED) {
            log.info("Payment already processed {}", payment.getId());
            return;
        }

        payment.setStatus(PaymentStatus.CAPTURED);
        payment.setRazorpayPaymentId(razorpayPaymentId);

        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.COMPLETED);

        log.info("Payment SUCCESS via webhook for booking {}", booking.getId());
    }

    private void handlePaymentFailed(JSONObject event) {

        JSONObject paymentEntity =
                event.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String razorpayOrderId = paymentEntity.getString("order_id");

        paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                .ifPresent(payment -> {
                    payment.setStatus(PaymentStatus.FAILED);
                    payment.getBooking().setStatus(BookingStatus.CANCELLED);
                    log.warn("Payment FAILED via webhook for booking {}",
                            payment.getBooking().getId());
                });
    }



}