package com.company.tradius_backend.controller;

import com.company.tradius_backend.dtos.ServiceSearchResponseDto;
import com.company.tradius_backend.entities.Location;
import com.company.tradius_backend.entities.ServiceOffering;
import com.company.tradius_backend.entities.Vendor;
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

    @GetMapping("/area/{areaId}")
    public List<ServiceSearchResponseDto> discoverServicesByArea(
            @PathVariable UUID areaId
    ) {
        return serviceOfferingRepository.findActiveServicesByArea(areaId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

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

    private ServiceSearchResponseDto mapToDto(ServiceOffering s){

        Vendor v = s.getVendor();
        Location l = v.getLocation();

        return new ServiceSearchResponseDto(
                s.getId(),
                s.getName(),
                s.getPrice(),
                s.getDurationMinutes(),
                v.getId(),
                v.getBusinessName(),
                l.getCity(),
                l.getArea()
        );
    }


    //  GET
    //  http://localhost:8090/discover/services/search?areaId={AREA_UUID}&q=salon
}
