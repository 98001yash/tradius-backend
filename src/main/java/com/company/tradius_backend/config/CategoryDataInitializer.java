package com.company.tradius_backend.config;


import com.company.tradius_backend.entities.Category;
import com.company.tradius_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CategoryDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;


    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count()>0 ){
            return;
        }
        List<Category> categories = List.of(
                Category.builder().name("Salon").build(),
                Category.builder().name("Tutor").build(),
                Category.builder().name("Home Repair").build(),
                Category.builder().name("Plumber").build(),
                Category.builder().name("Electrician").build()
        );

        categoryRepository.saveAll(categories);
    }
}
