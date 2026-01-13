package com.company.tradius_backend.service;

import com.company.tradius_backend.dtos.BookingResponseDto;
import com.company.tradius_backend.dtos.CreateBookingRequestDto;
import com.company.tradius_backend.enums.BookingStatus;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    // USER -> create a booking request
    BookingResponseDto createBooking(CreateBookingRequestDto request);


    // USER-> view own bookings
    List<BookingResponseDto> getMyBookings();


    // USER-> cancel bookings (only if allowed)
    BookingResponseDto cancelBooking(UUID bookingId);

    // VENDOR-> view bookings for own services
    List<BookingResponseDto> getVendorBookings();

    // VENDOR-? Filter bookings by status
    List<BookingResponseDto> getVendorBookingsByStatus(BookingStatus status);

    // VENDOR-> accept bookings
    BookingResponseDto acceptBooking(UUID bookingId);


    // VENDOR-> Reject bookings
    BookingResponseDto rejectBooking(UUID bookingId);

    // VENDOR -> Mark booking as completed
    BookingResponseDto completeBooking(UUID bookingId);
}
