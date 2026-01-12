package com.company.tradius_backend.service.Impl;

import com.company.tradius_backend.dtos.ApplyVendorRequestDto;
import com.company.tradius_backend.dtos.VendorResponseDto;
import com.company.tradius_backend.entities.Category;
import com.company.tradius_backend.entities.Location;
import com.company.tradius_backend.entities.User;
import com.company.tradius_backend.entities.Vendor;
import com.company.tradius_backend.enums.Role;
import com.company.tradius_backend.enums.VendorStatus;
import com.company.tradius_backend.exceptions.ResourceNotFoundException;
import com.company.tradius_backend.exceptions.RuntimeConflictException;
import com.company.tradius_backend.repository.CategoryRepository;
import com.company.tradius_backend.repository.UserRepository;
import com.company.tradius_backend.repository.VendorRepository;
import com.company.tradius_backend.service.VendorService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    @PreAuthorize("isAuthenticated()")
    public VendorResponseDto applyForVendor(ApplyVendorRequestDto request) {
        UUID userId = getCurrentUserId();
        log.info("User {} applying for vendor",userId);

        if(vendorRepository.existsByOwner_id(userId)){
            throw new RuntimeConflictException("User already has a vendor profile");
        }

        User owner =  userRepository.findById(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException("User not found "));

        Category category = categoryRepository
                .findByIdAndActiveTrue(request.getCategoryId())
                .orElseThrow(()->
                        new ResourceNotFoundException("Invalid or inactive category"));

        Location location = Location.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .city(request.getCity())
                .area(request.getArea())
                .addressLine(request.getAddressLine())
                .serviceRadiusKm(request.getServiceRadiusKm())
                .build();


        Vendor vendor = Vendor.builder()
                .owner(owner)
                .businessName(request.getBusinessName())
                .description(request.getDescription())
                .category(category)
                .location(location)
                .status(VendorStatus.PENDING)
                .active(true)
                .build();

        Vendor savedVendor = vendorRepository.save(vendor);
        return mapToResponseDto(savedVendor);
    }

    @Override
    @PreAuthorize("hasRole('VENDOR') or hasRole('ADMIN')")
    public VendorResponseDto getVendorProfile() {
       UUID userId = getCurrentUserId();

       Vendor vendor = vendorRepository
               .findByOwner_Id(userId)
               .orElseThrow(()->
                       new ResourceNotFoundException("Vendor Profile not found")
               );
       return mapToResponseDto(vendor);
    }

    //     ADMIN METHODS
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<VendorResponseDto> getPendingVendors() {
        return vendorRepository.findByStatus(VendorStatus.PENDING)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public VendorResponseDto approveVendor(UUID vendorId) {

        Vendor vendor = getVendorById(vendorId);

        if(vendor.getStatus()==VendorStatus.APPROVED){
            throw new RuntimeConflictException("Vendor already approved");
        }

        vendor.setStatus(VendorStatus.APPROVED);
        vendor.setActive(true);

        // upgrade user logic
        User owner = vendor.getOwner();
        if(owner.getRole() != Role.VENDOR){
            owner.setRole(Role.VENDOR);
            userRepository.save(owner);
        }


        log.info("Vendor {} approved and role upgraded for user {}", vendorId, owner.getId());
        return mapToResponseDto(vendor);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public VendorResponseDto rejectVendor(UUID vendorId) {
        Vendor vendor = getVendorById(vendorId);
        vendor.setStatus(VendorStatus.REJECTED);

        vendor.setActive(false);

        log.info("Vendor {} rejected", vendorId);
        return  mapToResponseDto(vendor);
    }

    // HELPER
    private UUID getCurrentUserId(){
        return UUID.fromString(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString()
        );
    }

    public Vendor getVendorById(UUID vendorId){
        return vendorRepository.findById(vendorId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Vendor not found"));
    }

    private VendorResponseDto mapToResponseDto(Vendor vendor){

        VendorResponseDto dto = modelMapper.map(vendor, VendorResponseDto.class);

        dto.setVendorId(vendor.getId());
        dto.setCategoryId(vendor.getCategory().getId());
        dto.setCategoryName(vendor.getCategory().getName());

        Location loc = vendor.getLocation();
        dto.setLatitude(loc.getLatitude());
        dto.setLongitude(loc.getLongitude());
        dto.setCity(loc.getCity());
        dto.setArea(loc.getArea());
        dto.setAddressLine(loc.getAddressLine());
        dto.setServiceRadiusKm(loc.getServiceRadiusKm());

        dto.setStatus(vendor.getStatus().name());
        dto.setActive(vendor.isActive());

        return dto;
    }
}
