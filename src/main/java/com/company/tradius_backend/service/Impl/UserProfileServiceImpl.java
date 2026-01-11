package com.company.tradius_backend.service.Impl;


import com.company.tradius_backend.dtos.UserProfileResponseDto;
import com.company.tradius_backend.dtos.UserProfileUpdateRequestDto;
import com.company.tradius_backend.entities.User;
import com.company.tradius_backend.entities.UserProfile;
import com.company.tradius_backend.exceptions.ResourceNotFoundException;
import com.company.tradius_backend.repository.UserProfileRepository;
import com.company.tradius_backend.repository.UserRepository;
import com.company.tradius_backend.service.UserProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserProfileResponseDto getProfile() {
        UUID userId = getCurrentUserId();

        UserProfile profile = userProfileRepository
                .findByUser_Id(userId)
                .orElseGet(()->createEmptyProfile(userId));
        return mapToDto(profile);
    }

    @Override
    public UserProfileResponseDto updateProfile(UserProfileUpdateRequestDto request) {
        UUID userId = getCurrentUserId();
        UserProfile profile = userProfileRepository
                .findByUser_Id(userId)
                .orElseGet(()->createEmptyProfile(userId));

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setProfileImageUrl(request.getProfileImageUrl());

        return mapToDto(userProfileRepository.save(profile));
    }

    @Override
    public UserProfileResponseDto getUserProfileById(UUID userId) {
        UserProfile profile = userProfileRepository
                .findByUser_Id(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Profile not found for userId={}" + userId));

        return mapToDto(profile);
    }

    // Helper
    private UUID getCurrentUserId(){
        Object principal = SecurityContextHolder.getContext().
                getAuthentication().
                getPrincipal();

        return UUID.fromString(principal.toString());
    }

    private UserProfile createEmptyProfile(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->
                        new ResourceNotFoundException("User not found with id="+userId));

        UserProfile profile = new UserProfile();
        profile.setUser(user);

        return userProfileRepository.save(profile);
    }

    private UserProfileResponseDto mapToDto(UserProfile profile){
        UserProfileResponseDto dto =
                modelMapper.map(profile, UserProfileResponseDto.class);
        dto.setUserId(profile.getUser().getId());
        return dto;
    }
}
