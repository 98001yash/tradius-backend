package com.company.tradius_backend.service.Impl;


import com.company.tradius_backend.dtos.CreateServiceOfferingRequestDto;
import com.company.tradius_backend.dtos.ServiceOfferingResponseDto;
import com.company.tradius_backend.service.ServiceOfferingService;
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

    @Override
    public ServiceOfferingResponseDto createService(CreateServiceOfferingRequestDto request) {
        return null;
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
}
