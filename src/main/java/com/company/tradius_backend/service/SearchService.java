package com.company.tradius_backend.service;

import java.util.UUID;

public interface SearchService {

    Object search(
            UUID areaId,
            String q,
            UUID categoryId,
            Double minPrice,
            Double maxPrice,
            String type
    );
}
