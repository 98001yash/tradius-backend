package com.company.tradius_backend.controller;


import com.company.tradius_backend.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class FileUploadController {

    private final ImageUploadService imageUploadService;


    @PostMapping("/profile-image")
    public Map<String, String> uploadProfileImage(
            @RequestParam("file")MultipartFile file
            ){
        String uri = imageUploadService.upload(file);
        return Map.of("uri", uri);
    }
}
