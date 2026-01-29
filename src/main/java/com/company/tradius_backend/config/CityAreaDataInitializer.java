package com.company.tradius_backend.config;

import com.company.tradius_backend.entities.Area;
import com.company.tradius_backend.entities.City;
import com.company.tradius_backend.repository.AreaRepository;
import com.company.tradius_backend.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CityAreaDataInitializer implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final AreaRepository areaRepository;

    @Override
    public void run(String... args) {

        // ✅ Prevent duplicate seeding
        if (cityRepository.count() > 0) {
            return;
        }

        // ✅ Create Bangalore City
        City bangalore = City.builder()
                .name("Bangalore")
                .build();

        cityRepository.save(bangalore);

        // ✅ Add Areas inside Bangalore
        List<Area> areas = List.of(
                Area.builder().name("Gaya").city(bangalore).build(),
                Area.builder().name("Patna").city(bangalore).build(),
                Area.builder().name("Aurangabad").city(bangalore).build(),
                Area.builder().name("Bihar sarif").city(bangalore).build()
        );

        areaRepository.saveAll(areas);

        System.out.println("✅ City + Areas Seeded Successfully!");
    }
}
