package com.company.tradius_backend.service.Impl;


import com.company.tradius_backend.dtos.CreateServiceOfferingRequestDto;
import com.company.tradius_backend.dtos.ServiceOfferingResponseDto;
import com.company.tradius_backend.entities.ServiceOffering;
import com.company.tradius_backend.entities.Vendor;
import com.company.tradius_backend.enums.VendorStatus;
import com.company.tradius_backend.exceptions.ResourceNotFoundException;
import com.company.tradius_backend.exceptions.RuntimeConflictException;
import com.company.tradius_backend.repository.ServiceOfferingRepository;
import com.company.tradius_backend.repository.VendorRepository;
import com.company.tradius_backend.service.ServiceOfferingService;
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
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;
    private final VendorRepository vendorRepository;
    private final ModelMapper modelMapper;

    //  vendor methods

    @Override
    @PreAuthorize("hasRole('VENDOR')")
    public ServiceOfferingResponseDto createService(CreateServiceOfferingRequestDto request) {
        Vendor vendor = gerApprovedVendorForCurrentUser();

        ServiceOffering service = ServiceOffering.builder()
                .vendor(vendor)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .active(true)
                .build();

        ServiceOffering savedService = serviceOfferingRepository.save(service);
        log.info("Service {} created for vendor {}",savedService.getId(), vendor.getId());

        return mapToResponseDto(savedService);


    }

    @Override
    public List<ServiceOfferingResponseDto> getMyServices() {
        return List.of();
    }

    @Override
    public List<ServiceOfferingResponseDto> getActiveServicesByVendor(UUID vendorId) {
        return List.of();
    }

    @Override
    public ServiceOfferingResponseDto deactivateService(UUID serviceId) {
        return null;
    }

    @Override
    public ServiceOfferingResponseDto activateService(UUID serviceId) {
        return null;
    }

    //  Helpers
    private Vendor gerApprovedVendorForCurrentUser(){
        UUID userId = UUID.fromString(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()
                        .toString()
        );
        Vendor vendor = vendorRepository
                .findByOwner_Id(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Vendor profile not found")
                );
        if(vendor.getStatus()!= VendorStatus.APPROVED){
            throw new RuntimeConflictException("Vendor is not approved yet");
        }
        return vendor;
    }

    private ServiceOfferingResponseDto mapToResponseDto(ServiceOffering service){
        ServiceOfferingResponseDto dto = modelMapper.map(service, ServiceOfferingResponseDto.class);

        dto.setServiceId(service.getId());
        dto.setVendorId(service.getVendor().getId());
        return dto;
    }
}
