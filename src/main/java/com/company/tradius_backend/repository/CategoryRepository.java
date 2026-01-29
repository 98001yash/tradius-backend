package com.company.tradius_backend.repository;

import com.company.tradius_backend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findByIdAndActiveTrue(UUID id);

    boolean existsByNameIgnoreCase(String name);

    List<Category> findByActiveTrue();

}
