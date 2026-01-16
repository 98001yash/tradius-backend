package com.company.tradius_backend.controller;

import com.company.tradius_backend.dtos.ServiceSearchResponseDto;
import com.company.tradius_backend.entities.*;
import com.company.tradius_backend.repository.ServiceOfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/discover/services")
@RequiredArgsConstructor
public class ServiceDiscoveryController {

    private final ServiceOfferingRepository serviceOfferingRepository;

    // Example: /discover/services/area/Manpur
    @GetMapping("/area/{areaId}")
    public List<ServiceSearchResponseDto> discoverServicesByArea(
            @PathVariable UUID areaId
    ) {
        return serviceOfferingRepository.findActiveServicesByArea(areaId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    // Example: /discover/services/search?area=Manpur&q=salon
    @GetMapping("/search")
    public List<ServiceSearchResponseDto> searchServices(
            @RequestParam UUID areaId,
            @RequestParam String q
    ) {
        return serviceOfferingRepository.searchServicesInArea(areaId, q)
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    private ServiceSearchResponseDto mapToDto(ServiceOffering s) {

        Vendor v = s.getVendor();
        Location l = v.getLocation();
        Area a = l.getArea();
        City c = a.getCity();

        return new ServiceSearchResponseDto(
                s.getId(),
                s.getName(),
                s.getPrice(),
                v.getBusinessName(),
                a.getName()      // area name
                // city name
        );
    }

    //http://localhost:8090/discover/services/search
  //          ?areaId=9d3c8c5e-9c12-4c1f-bc6f-3e1c8a7d5a21
//&q=hair

}
