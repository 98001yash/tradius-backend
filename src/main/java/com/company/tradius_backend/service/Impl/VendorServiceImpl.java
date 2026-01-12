package com.company.tradius_backend.service.Impl;

import com.company.tradius_backend.dtos.ApplyVendorRequestDto;
import com.company.tradius_backend.dtos.VendorResponseDto;
import com.company.tradius_backend.entities.Category;
import com.company.tradius_backend.entities.Location;
import com.company.tradius_backend.entities.User;
import com.company.tradius_backend.entities.Vendor;
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

    @Override
    public List<VendorResponseDto> getPendingVendors() {
        return List.of();
    }

    @Override
    public VendorResponseDto approveVendor(UUID vendorId) {
        return null;
    }

    @Override
    public VendorResponseDto rejectVendor(UUID vendorId) {
        return null;
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
        dto.setArea(loc.getAddressLine());
        dto.setServiceRadiusKm(loc.getServiceRadiusKm());

        dto.setStatus(vendor.getStatus().name());
        dto.setActive(vendor.isActive());

        return dto;
    }
}
