package com.company.tradius_backend.repository;

import com.company.tradius_backend.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByBooking_Id(UUID bookingId);

    Optional<Payment> findByRazorpayOrderId(String orderId);
}
