package com.company.tradius_backend.repository;

import com.company.tradius_backend.entities.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, UUID> {

    // vendor dashboard: get all the services  of the vendor
    List<ServiceOffering> findByVendor_id(UUID vendorId);

    //  get only active services of a vendor
    List<ServiceOffering> findByVendor_IdAndActiveTrue(UUID vendorId);

    // ownerShip and existence check
    Optional<ServiceOffering> findByIdAndVendor_Id(UUID id, UUID vendorId);


    @Query("""
    SELECT s FROM ServiceOffering s
    JOIN s.vendor v
    JOIN v.location l
    JOIN l.area a
    WHERE a.id = :areaId
      AND s.active = true
      AND v.active = true
      AND v.status = 'APPROVED'
""")
    List<ServiceOffering> findActiveServicesByArea(
            @Param("areaId") UUID areaId
    );



    @Query("""
    SELECT s FROM ServiceOffering s
    JOIN s.vendor v
    JOIN v.location l
    JOIN l.area a
    WHERE a.id = :areaId
      AND s.active = true
      AND v.active = true
      AND v.status = 'APPROVED'
      AND (
           lower(s.name) LIKE lower(concat('%', :q, '%'))
        OR lower(s.description) LIKE lower(concat('%', :q, '%'))
        OR lower(v.businessName) LIKE lower(concat('%', :q, '%'))
      )
""")
    List<ServiceOffering> searchServicesInArea(
            @Param("areaId") UUID areaId,
            @Param("q") String q
    );


    @Query("""
SELECT s FROM ServiceOffering s
JOIN s.vendor v
JOIN v.location l
WHERE l.area.id = :areaId
  AND s.active = true
  AND v.active = true
  AND v.status = 'APPROVED'
ORDER BY s.createdAt DESC
""")
    List<ServiceOffering> findPopularServicesByArea(UUID areaId);



    @Query("""
SELECT s FROM ServiceOffering s
JOIN s.vendor v
JOIN v.location l
WHERE l.area.id = :areaId
  AND s.active = true
  AND v.active = true
  AND v.status = 'APPROVED'
  AND (:categoryId IS NULL OR v.category.id = :categoryId)
  AND (:minPrice IS NULL OR s.price >= :minPrice)
  AND (:maxPrice IS NULL OR s.price <= :maxPrice)
  AND (
      :q IS NULL OR
      lower(s.name) LIKE lower(concat('%', :q, '%')) OR
      lower(v.businessName) LIKE lower(concat('%', :q, '%'))
  )
""")
    List<ServiceOffering> smartSearchServices(
            UUID areaId,
            UUID categoryId,
            Double minPrice,
            Double maxPrice,
            String q
    );



}
