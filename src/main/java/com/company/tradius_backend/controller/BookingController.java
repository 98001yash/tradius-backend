package com.company.tradius_backend.controller;

import com.company.tradius_backend.dtos.BookingResponseDto;
import com.company.tradius_backend.dtos.CreateBookingRequestDto;
import com.company.tradius_backend.enums.BookingStatus;
import com.company.tradius_backend.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    //    USER APIs

    // USER-> Create a booking

    @PostMapping
    public BookingResponseDto createBooking(@Valid @RequestBody CreateBookingRequestDto request){
        return bookingService.createBooking(request);
    }

    // USER -> get My Bookings
    @GetMapping("/me")
    public List<BookingResponseDto> getMyBookings(){
        return bookingService.getMyBookings();
    }

    // USER -> cancel a booking
    @PutMapping("/{bookingId}/cancel")
    public BookingResponseDto cancelBBooking(@PathVariable UUID bookingId){
        return bookingService.cancelBooking(bookingId);
    }

    // VENDOR API

    // VENDOR ->  GET ALL BOOKINGS FOR MY SERVICES

    @GetMapping("/vendor")
    public List<BookingResponseDto> getVendorBookings(){
        return bookingService.getVendorBookings();
    }

    // VENDOR -> Get bookings by status
    @GetMapping("/vendor/status/{status}")
    public List<BookingResponseDto> getVendorBookingByStatus(@PathVariable BookingStatus status){
        return bookingService.getVendorBookingsByStatus(status);
    }

    // VENDOR -> Accept bookings
    @PutMapping("/{bookingId}/accept")
    public BookingResponseDto acceptBooking(@PathVariable UUID bookingId){
        return bookingService.acceptBooking(bookingId);
    }

    // VENDOR -> Reject booking
    @PutMapping("/{bookingId}/reject")
    public BookingResponseDto rejectBooking(@PathVariable UUID bookingId){
        return bookingService.rejectBooking(bookingId);
    }

    // VENDOR -> complete bookings
    @PutMapping("/{bookingId}/complete")
    public BookingResponseDto completeBooking(@PathVariable UUID bookingId){
        return bookingService.completeBooking(bookingId);
    }
}
