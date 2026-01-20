package com.company.tradius_backend.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageUploadService {

    private static final String UPLOAD_DIR = "uploads/profile-images/";

    public String upload(MultipartFile file){
        try{
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String filename =
                    UUID.randomUUID() + "_"+ file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIR + filename);
            Files.write(path, file.getBytes());

            return "http://localhost:8090/" + UPLOAD_DIR+ filename;
        }catch(IOException e){
            throw new RuntimeException("image upload failed");
        }
    }
}
