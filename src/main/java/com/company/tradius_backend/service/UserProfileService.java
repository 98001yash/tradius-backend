package com.company.tradius_backend.service;

import com.company.tradius_backend.dtos.UserProfileResponseDto;
import com.company.tradius_backend.dtos.UserProfileUpdateRequestDto;

public interface UserProfileService {


    // get Profile of the currently authenticated User
    UserProfileResponseDto getProfile();

    // update  Profile of the currently Authenticated User
    UserProfileResponseDto updateProfile(UserProfileUpdateRequestDto request);


    // get profile of a user by userId ( ADMIN case)
    UserProfileResponseDto getUserProfileById(Long userId);
}
