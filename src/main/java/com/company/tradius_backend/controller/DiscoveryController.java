package com.company.tradius_backend.controller;

import com.company.tradius_backend.dtos.VendorDiscoveryDto;
import com.company.tradius_backend.entities.Vendor;
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
                .map(v -> new VendorDiscoveryDto(
                        v.getId(),
                        v.getBusinessName(),
                        v.getCategory().getName(),
                        v.getLocation().getArea(),
                        v.getLocation().getCity()
                ))
                .toList();
    }

}
