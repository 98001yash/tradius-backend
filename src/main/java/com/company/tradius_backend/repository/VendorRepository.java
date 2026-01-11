package com.company.tradius_backend.repository;

import com.company.tradius_backend.entities.Vendor;
import com.company.tradius_backend.enums.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    Optional<Vendor>  findByOwner_Id(UUID ownerId);

    boolean existsByOwner_id(UUID ownerId);

    List<Vendor> findByStatus(VendorStatus status);
}
