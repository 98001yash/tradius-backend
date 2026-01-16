package com.company.tradius_backend.controller;

import com.company.tradius_backend.entities.Area;
import com.company.tradius_backend.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/areas")
@RequiredArgsConstructor
public class AreaController {

    private final AreaRepository areaRepository;

    @GetMapping("/city/{cityId}")
    public List<Area> getAreasByCity(@PathVariable UUID cityId) {
        return areaRepository.findByCity_Id(cityId);
    }
}

