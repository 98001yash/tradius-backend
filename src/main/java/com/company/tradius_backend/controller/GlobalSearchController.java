package com.company.tradius_backend.controller;

import com.company.tradius_backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/discover/search")
@RequiredArgsConstructor
public class GlobalSearchController {

    private final SearchService searchService;

    @GetMapping
    public Object search(
            @RequestParam UUID areaId,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "service") String type
    ) {
        return searchService.search(
                areaId, q, categoryId, minPrice, maxPrice, type
        );
    }
}


/**


GET /discover/search?areaId=UUID&q=salon
GET /discover/search?areaId=UUID&type=vendor
GET /discover/search?areaId=UUID&categoryId=UUID
GET /discover/search?areaId=UUID&minPrice=100&maxPrice=300

**/