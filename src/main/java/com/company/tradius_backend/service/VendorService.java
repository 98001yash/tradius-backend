package com.company.tradius_backend.service;

import com.company.tradius_backend.dtos.ApplyVendorRequestDto;
import com.company.tradius_backend.dtos.VendorResponseDto;

import java.util.List;
import java.util.UUID;

public interface VendorService {

    // apply to become  a vendor ( USER - > VENDOR candidates
    VendorResponseDto applyForVendor(ApplyVendorRequestDto request);

    // get vendor profile of the currently authenticated USER
    VendorResponseDto getVendorProfile();

    // ADMIN:  get All vendors pending approval
    List<VendorResponseDto> getPendingVendors();

    // Admin: Approve a vendor
    VendorResponseDto approveVendor(UUID vendorId);

    // ADMIN; Reject a vendor
    VendorResponseDto rejectVendor(UUID vendorId);
}
