package com.company.tradius_backend.controller;

import com.company.tradius_backend.dtos.ServiceSearchResponseDto;
import com.company.tradius_backend.dtos.VendorDiscoveryDto;
import com.company.tradius_backend.entities.*;
import com.company.tradius_backend.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/discover")
@RequiredArgsConstructor
public class DiscoveryController {

    private final VendorRepository vendorRepository;

    @GetMapping("/area/{areaId}/vendors")
    public List<VendorDiscoveryDto> discoverVendorsByArea(@PathVariable UUID areaId) {
        return vendorRepository.findActiveVendorsByArea(areaId)
                .stream()
                .map(v -> {
                    Area area = v.getLocation().getArea();
                    City city = area.getCity();

                    return new VendorDiscoveryDto(
                            v.getId(),
                            v.getBusinessName(),
                            v.getCategory().getName(),
                            area.getId(),
                            area.getName(),
                            city.getId(),
                            city.getName()
                    );
                })
                .toList();
    }

}
