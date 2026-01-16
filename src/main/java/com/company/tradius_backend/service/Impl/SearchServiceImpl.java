package com.company.tradius_backend.service.Impl;


import com.company.tradius_backend.dtos.ServiceSearchResponseDto;
import com.company.tradius_backend.dtos.VendorDiscoveryDto;
import com.company.tradius_backend.repository.ServiceOfferingRepository;
import com.company.tradius_backend.repository.VendorRepository;
import com.company.tradius_backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ServiceOfferingRepository serviceRepo;
    private final VendorRepository vendorRepo;

    @Override
    public Object search(
            UUID areaId,
            String q,
            UUID categoryId,
            Double minPrice,
            Double maxPrice,
            String type
    ) {
        if ("vendor".equalsIgnoreCase(type)) {
            return vendorRepo.smartSearchVendors(areaId, categoryId, q)
                    .stream()
                    .map(v -> new VendorDiscoveryDto(
                            v.getId(),
                            v.getBusinessName(),
                            v.getCategory().getName(),
                            v.getLocation().getArea().getName(),
                            v.getLocation().getArea().getCity().getName()
                    ))
                    .toList();
        }

        return serviceRepo.smartSearchServices(
                        areaId, categoryId, minPrice, maxPrice, q
                )
                .stream()
                .map(s -> new ServiceSearchResponseDto(
                        s.getId(),
                        s.getName(),
                        s.getPrice(),
                        s.getVendor().getBusinessName(),
                        s.getVendor().getLocation().getArea().getName()
                ))
                .toList();
    }
}
