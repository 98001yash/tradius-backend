package com.company.tradius_backend.repository;

import com.company.tradius_backend.entities.Category;
import com.company.tradius_backend.entities.ServiceOffering;
import com.company.tradius_backend.entities.Vendor;
import com.company.tradius_backend.enums.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    Optional<Vendor>  findByOwner_Id(UUID ownerId);

    boolean existsByOwner_id(UUID ownerId);

    List<Vendor> findByStatus(VendorStatus status);


    @Query("""
    SELECT v FROM Vendor v
    JOIN v.location l
    JOIN l.area a
    WHERE a.id = :areaId
      AND v.status = 'APPROVED'
      AND v.active = true
""")
    List<Vendor> findActiveVendorsByArea(UUID areaId);


    @Query("""
SELECT DISTINCT c FROM Vendor v
JOIN v.category c
JOIN v.location l
WHERE l.area.id = :areaId
  AND v.status = 'APPROVED'
  AND v.active = true
""")
    List<Category> findCategoriesByArea(UUID areaId);



    @Query("""
SELECT v FROM Vendor v
JOIN v.location l
WHERE l.area.id = :areaId
  AND v.status = 'APPROVED'
  AND v.active = true
ORDER BY v.createdAt DESC
""")
    List<Vendor> findTopVendorsByArea(UUID areaId);



    @Query("""
SELECT v FROM Vendor v
JOIN v.location l
WHERE l.area.id = :areaId
  AND v.active = true
  AND v.status = 'APPROVED'
  AND (:categoryId IS NULL OR v.category.id = :categoryId)
  AND (
      :q IS NULL OR
      lower(v.businessName) LIKE lower(concat('%', :q, '%'))
  )
""")
    List<Vendor> smartSearchVendors(
            UUID areaId,
            UUID categoryId,
            String q
    );
}
