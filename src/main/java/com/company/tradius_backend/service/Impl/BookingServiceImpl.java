package com.company.tradius_backend.service.Impl;

import com.company.tradius_backend.dtos.BookingResponseDto;
import com.company.tradius_backend.dtos.CreateBookingRequestDto;
import com.company.tradius_backend.entities.Booking;
import com.company.tradius_backend.entities.ServiceOffering;
import com.company.tradius_backend.entities.User;
import com.company.tradius_backend.entities.Vendor;
import com.company.tradius_backend.enums.BookingStatus;
import com.company.tradius_backend.exceptions.ResourceNotFoundException;
import com.company.tradius_backend.exceptions.RuntimeConflictException;
import com.company.tradius_backend.repository.BookingRepository;
import com.company.tradius_backend.repository.ServiceOfferingRepository;
import com.company.tradius_backend.repository.UserRepository;
import com.company.tradius_backend.repository.VendorRepository;
import com.company.tradius_backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    //   USER ACTIONS

    @Override
    @PreAuthorize("hasRole('USER')")
    public BookingResponseDto createBooking(CreateBookingRequestDto request) {
        UUID userId = getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        ServiceOffering service = serviceOfferingRepository.findById(request.getServiceId())
                .orElseThrow(()->new ResourceNotFoundException("Service not found"));

        if(!service.isActive()){
            throw new RuntimeConflictException("Service is not available");
        }

        Vendor vendor = service.getVendor();

        Booking booking = Booking.builder()
                .user(user)
                .vendor(vendor)
                .serviceOffering(service)
                .scheduledAt(request.getScheduledAt())
                .notes(request.getNotes())
                .build();

        Booking saved = bookingRepository.save(booking);

        log.info("Booking {} created by user {} for service {}",
                saved.getId(), userId, service.getId());

        return mapToDto(saved);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<BookingResponseDto> getMyBookings(UUID serviceId) {
        UUID userId = getCurrentUserId();

        return bookingRepository.findByUser_Id(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public BookingResponseDto cancelBooking(UUID bookingId) {
        UUID userId = getCurrentUserId();
        Booking booking = bookingRepository
                .findByIdAndUser_Id(bookingId, userId)
                .orElseThrow(()->new ResourceNotFoundException("Booking not found"));

        if(booking.getStatus() !=BookingStatus.REQUESTED){
            throw new RuntimeConflictException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        log.info("Booking {} cancelled by user {}",bookingId, userId);
        return mapToDto(booking);


        //   VENDOR ACTIONS

    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public List<BookingResponseDto> getVendorBookings() {
       Vendor vendor = getCurrentVendor();

       return bookingRepository.findByVendor_Id(vendor.getId())
               .stream()
               .map(this::mapToDto)
               .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public List<BookingResponseDto> getVendorBookingsByStatus(BookingStatus status) {
       Vendor vendor = getCurrentVendor();

       return bookingRepository.findByVendor_IdAndStatus(vendor.getId(), status)
               .stream()
               .map(this::mapToDto)
               .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public BookingResponseDto acceptBooking(UUID bookingId) {
        Booking booking = getVendorBooking(bookingId);
        if(booking.getStatus() != BookingStatus.REQUESTED){
            throw new RuntimeConflictException("Booking cannot be accepted");
        }
        booking.setStatus(BookingStatus.ACCEPTED);

        log.info("Booking {} accepted by vendor {}",bookingId, booking.getVendor().getId());
        return mapToDto(booking);
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public BookingResponseDto rejectBooking(UUID bookingId) {
        Booking booking = getVendorBooking(bookingId);

        if(booking.getStatus() != BookingStatus.REQUESTED){
            throw new RuntimeConflictException("Booking cannot be rejected");
        }
        booking.setStatus(BookingStatus.REJECTED);

        log.info("Booking {} rejected by vendor {}",bookingId, booking.getVendor().getId());
        return mapToDto(booking);
    }

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public BookingResponseDto completeBooking(UUID bookingId) {

        Booking booking = getVendorBooking(bookingId);
        if(booking.getStatus() != BookingStatus.ACCEPTED){
            throw new RuntimeConflictException("Only accepted booking can be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        log.info("Booking {} completed by vendor {}",bookingId, booking.getVendor().getId());

        return mapToDto(booking);

    }

    //  HELPERS
    private UUID getCurrentUserId(){
        return UUID.fromString(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString()
        );
    }

    private Vendor getCurrentVendor(){
        UUID userId = getCurrentUserId();

                return vendorRepository.findByOwner_Id(userId)
                        .orElseThrow(()->
                                new ResourceNotFoundException("Vendor profile not found"));
    }


    private Booking getVendorBooking(UUID bookingId){

        Vendor vendor = getCurrentVendor();

        return bookingRepository
                .findByIdAndVendor_Id(bookingId, vendor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }


    private BookingResponseDto mapToDto(Booking booking){
        BookingResponseDto dto =
                modelMapper.map(booking, BookingResponseDto.class);
        dto.setBookingId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setVendorId(booking.getVendor().getId());
        dto.setServiceId(booking.getServiceOffering().getId());

        dto.setServiceName(booking.getServiceOffering().getName());
        dto.setServicePrice(booking.getServiceOffering().getPrice());

        return dto;
    }
}
