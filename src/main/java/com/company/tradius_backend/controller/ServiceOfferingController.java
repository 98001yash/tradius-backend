package com.company.tradius_backend.controller;

import com.company.tradius_backend.dtos.CreateServiceOfferingRequestDto;
import com.company.tradius_backend.dtos.ServiceOfferingResponseDto;
import com.company.tradius_backend.entities.ServiceOffering;
import com.company.tradius_backend.service.ServiceOfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceOfferingController {


    private final ServiceOfferingService serviceOfferingService;

    // VENDOR -> create  a new service offering
    @PostMapping
    public ServiceOfferingResponseDto createService(@Valid @RequestBody CreateServiceOfferingRequestDto request){
        return serviceOfferingService.createService(request);
    }

    // VENDOR -> Get all services created by the logged in vendor
    @GetMapping("/me")
    public List<ServiceOfferingResponseDto> getMyService(){
        return serviceOfferingService.getMyServices();
    }

    // VENDOR -> Deactivate a services
    @PutMapping("/{serviceId}/deactivate")
    public ServiceOfferingResponseDto deactivateService(@PathVariable UUID serviceId){
        return serviceOfferingService.deactivateService(serviceId);
    }

    // VENDOR -> activate a service
    @PutMapping("/{serviceId}/activate")
    public ServiceOfferingResponseDto activateService(@PathVariable UUID serviceId){
        return serviceOfferingService.activateService(serviceId);
    }

   // PUBLIC/ USER -> Get  active services of a vendor
    @GetMapping("/vendor/{vendorId}")
    public List<ServiceOfferingResponseDto> getActiveServicesByVendor(
            @PathVariable UUID vendorId
    ) {
        return serviceOfferingService.getActiveServicesByVendor(vendorId);
    }
}
