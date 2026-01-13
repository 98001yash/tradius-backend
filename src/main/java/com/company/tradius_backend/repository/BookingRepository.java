package com.company.tradius_backend.repository;

import com.company.tradius_backend.entities.Booking;
import com.company.tradius_backend.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    // USER -> get all bookings by a user
    List<Booking> findByUser_Id(UUID userId);

    // VENDOR-> Get all bookings for a vendor
    List<Booking> findByVendor_Id(UUID vendorId);

    // VENDOR -> Get bookings by status
    List<Booking> findByVendor_IdAndStatus(UUID vendorId, BookingStatus status);

    // ownership + existence checkyess
    //(USER)
    Optional<Booking> findByIdAndUser_Id(UUID bookingId, UUID userId);

    // Ownership+ existence check(VENDOR_)
    Optional<Booking> findByIdAndVendor_Id(UUID bookingId, UUID vendorId);
}
