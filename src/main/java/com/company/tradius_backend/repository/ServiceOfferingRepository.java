package com.company.tradius_backend.repository;

import com.company.tradius_backend.entities.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, UUID> {

    // vendor dashboard: get all the services  of the vendor
    List<ServiceOffering> findByVendor_id(UUID vendorId);

    //  get only active services of a vendor
    List<ServiceOffering> findByVendor_IdAndActiveTrue(UUID vendorId);

    // ownerShip and existence check
    Optional<ServiceOffering> findByIdVendor_Id(UUID id, UUID vendorId);
}
