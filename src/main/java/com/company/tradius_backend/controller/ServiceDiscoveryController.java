package com.company.tradius_backend.controller;

import com.company.tradius_backend.entities.ServiceOffering;
import com.company.tradius_backend.repository.ServiceOfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/discover/services")
@RequiredArgsConstructor
public class ServiceDiscoveryController {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @GetMapping("/area/{areaId}")
    public List<ServiceOffering> discoverServicesByArea(
            @PathVariable UUID areaId
    ) {
        return serviceOfferingRepository.findActiveServicesByArea(areaId);
    }
}
