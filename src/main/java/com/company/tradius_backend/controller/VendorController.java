package com.company.tradius_backend.controller;


import com.company.tradius_backend.dtos.ApplyVendorRequestDto;
import com.company.tradius_backend.dtos.VendorResponseDto;
import com.company.tradius_backend.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    // User -> apply to become the vendor
    @PostMapping("/apply")
    public VendorResponseDto applyVendor(
            @Valid @RequestBody ApplyVendorRequestDto request
            ){
        return vendorService.applyForVendor(request);
    }

    // VENDOR/ ADMIN -> get own vendor profiles
    @GetMapping("/me")
    public VendorResponseDto getMyVendorProfiles(){
        return vendorService.getVendorProfile();
    }

    // ADMIN-> get all pending vendor
    @GetMapping("/admin/pending")
    public List<VendorResponseDto> getPendingVendors(){
        return vendorService.getPendingVendors();
    }

    // ADMIN -> approve vendor
    @PutMapping("/admin/{vendorId}/approve")
    public VendorResponseDto approveVendor(
            @PathVariable UUID vendorId
    ){
        return vendorService.approveVendor(vendorId);
    }

    // ADMIN -> Reject vendor
    @PutMapping("/admin/{vendorId}/reject")
    public VendorResponseDto rejectVendor(
            @PathVariable UUID vendorId
    ){
        return vendorService.rejectVendor(vendorId);
    }
}
