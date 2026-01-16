package com.company.tradius_backend.repository;

import com.company.tradius_backend.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AreaRepository extends JpaRepository<Area, UUID> {

    List<Area> findByCity_Id(UUID cityId);

    Optional<Area> findByNameIgnoreCaseAndCity_Id(String name, UUID cityId);
}
