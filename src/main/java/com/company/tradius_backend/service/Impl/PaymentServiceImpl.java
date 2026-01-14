package com.company.tradius_backend.service.Impl;


import com.company.tradius_backend.dtos.CreatePaymentResponseDto;
import com.company.tradius_backend.dtos.VerifyPaymentRequestDto;
import com.company.tradius_backend.entities.Booking;
import com.company.tradius_backend.entities.Payment;
import com.company.tradius_backend.enums.BookingStatus;
import com.company.tradius_backend.enums.PaymentMethod;
import com.company.tradius_backend.enums.PaymentStatus;
import com.company.tradius_backend.exceptions.ResourceNotFoundException;
import com.company.tradius_backend.exceptions.RuntimeConflictException;
import com.company.tradius_backend.repository.BookingRepository;
import com.company.tradius_backend.repository.PaymentRepository;
import com.company.tradius_backend.service.PaymentService;
import com.company.tradius_backend.utils.RazorpaySignatureVerifier;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpayClient;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Value("${razorpay.key.secret}")
    private String razorpaySecret;


    @Override
    @PreAuthorize("hasRole('USER')")
    public CreatePaymentResponseDto createRazorpayOrder(UUID bookingId) throws RazorpayException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new RuntimeConflictException(
                    "Payment can be initiated only after vendor accepts the booking"
            );
        }
        paymentRepository.findByBooking_Id(bookingId)
                .ifPresent(p -> {
                    throw new RuntimeConflictException("Payment already initiated");
                });

        long amountInPaise = (long) (booking.getServiceOffering().getPrice() * 100);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", booking.getId().toString());

        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(amountInPaise)
                .method(PaymentMethod.RAZORPAY)
                .status(PaymentStatus.ORDER_CREATED)
                .razorpayOrderId(razorpayOrder.get("id"))
                .build();


        paymentRepository.save(payment);

        log.info("Razorpay order {} created for booking {}",
                razorpayOrder.get("id"), bookingId);

        return new CreatePaymentResponseDto(
                razorpayOrder.get("id"),
                amountInPaise,
                "INR"
        );
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void verifyRazorpayPayment(VerifyPaymentRequestDto request) throws RazorpayException {

        Payment payment = paymentRepository
                .findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(()->new ResourceAccessException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.ORDER_CREATED) {
            throw new RuntimeConflictException("Payment already initiated");
        }

        boolean isValid = RazorpaySignatureVerifier.verify(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature(),
                razorpaySecret
        );

        if(!isValid){
            payment.setStatus(PaymentStatus.FAILED);
            throw new RuntimeConflictException("Invalid payment signature");
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setStatus(PaymentStatus.CAPTURED);

        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.COMPLETED);

        log.info(
                "Payment successful. Booking {} confirmed. PaymentId={}",
                booking.getId(),
                request.getRazorpayPaymentId()
        );
    }
}
