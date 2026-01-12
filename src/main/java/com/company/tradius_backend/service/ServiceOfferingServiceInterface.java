package com.company.tradius_backend.service;

import com.company.tradius_backend.dtos.CreateServiceOfferingRequestDto;
import com.company.tradius_backend.dtos.ServiceOfferingResponseDto;

import java.util.List;
import java.util.UUID;

public interface ServiceOfferingServiceInterface {


    // Vendor -> create a new service offering
    ServiceOfferingResponseDto createService(
            CreateServiceOfferingRequestDto request
    );

    // vendor -> Get all services created by the Logged in vendor
    List<ServiceOfferingResponseDto> getMyServices();

    // USER/PUBLIC -> Get active services of a vendor
    List<ServiceOfferingResponseDto> getActiveServicesByVendor(
            UUID vendorId
    );

    // Vendor -> deactivate  a service
    ServiceOfferingResponseDto deactivateService(
            UUID serviceId
    );

    // vendor -> activate a service
    ServiceOfferingResponseDto activateService(
            UUID serviceId
    );


}
