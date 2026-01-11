package com.company.tradius_backend.controller;

import com.company.tradius_backend.dtos.UserProfileResponseDto;
import com.company.tradius_backend.dtos.UserProfileUpdateRequestDto;
import com.company.tradius_backend.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public UserProfileResponseDto getMyProfile(){
        return userProfileService.getProfile();
    }

    @PutMapping("/me")
    public UserProfileResponseDto updateMyProfile(@Valid @RequestBody UserProfileUpdateRequestDto request){
        return userProfileService.updateProfile(request);
    }

    @GetMapping("/admin/{userId}")
    public UserProfileResponseDto getProfileByUserId(
            @PathVariable UUID userId
    ){
        return userProfileService.getUserProfileById(userId);
    }
}
