package com.company.tradius_backend.controller;


import com.company.tradius_backend.dtos.*;
import com.company.tradius_backend.entities.Area;
import com.company.tradius_backend.repository.AreaRepository;
import com.company.tradius_backend.repository.ServiceOfferingRepository;
import com.company.tradius_backend.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/discover/home")
@RequiredArgsConstructor
public class HomeDiscoveryController {

    private final AreaRepository areaRepository;
    private final VendorRepository vendorRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;

    @GetMapping
    public HomeDiscoveryResponseDto discoverHome(@RequestParam UUID areaId) {

        Area area = areaRepository.findById(areaId)
                .orElseThrow(() -> new RuntimeException("Area not found"));

        AreaInfoDto areaInfo = new AreaInfoDto(
                area.getId(),
                area.getName(),
                area.getCity().getName()
        );

        var categories = vendorRepository.findCategoriesByArea(areaId)
                .stream()
                .map(c -> new CategoryDiscoveryDto(c.getId(), c.getName()))
                .toList();

        var vendors = vendorRepository.findTopVendorsByArea(areaId)
                .stream()
                .limit(6)
                .map(v -> new VendorHomeDto(
                        v.getId(),
                        v.getBusinessName(),
                        v.getCategory().getName()
                ))
                .toList();

        var services = serviceOfferingRepository.findPopularServicesByArea(areaId)
                .stream()
                .limit(6)
                .map(s -> new ServiceHomeDto(
                        s.getId(),
                        s.getName(),
                        s.getPrice()
                ))
                .toList();

        return new HomeDiscoveryResponseDto(
                areaInfo,
                categories,
                vendors,
                services
        );
    }
}
