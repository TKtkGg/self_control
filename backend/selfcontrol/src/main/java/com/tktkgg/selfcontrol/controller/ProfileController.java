package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

import com.tktkgg.selfcontrol.dto.response.ProfileResponse;
import com.tktkgg.selfcontrol.dto.request.UpdateProfileRequest;
import com.tktkgg.selfcontrol.service.user.ProfileService;

@RestController
@RequestMapping("/api")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile() {
        return profileService.getMyProfile();
    }

    @GetMapping("/users/{userId}/profile")
    public ProfileResponse getProfile(@PathVariable UUID userId) {
        return profileService.getUserProfile(userId);
    }

    @PatchMapping("/profile")
    public ProfileResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return profileService.updateMyProfile(request);
    }
}
