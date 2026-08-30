package com.tktkgg.selfcontrol.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.tktkgg.selfcontrol.dto.response.ProfileResponse;
import com.tktkgg.selfcontrol.dto.request.UpdateProfileRequest;
import com.tktkgg.selfcontrol.service.ProfileService;

@RestController
public class SettingController {
    private final ProfileService profileService;

    public SettingController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/api/setting/profile/{userId}")
    public ProfileResponse getProfile(@PathVariable UUID userId) {
        return profileService.getProfile(userId);
    }

    @PutMapping("/api/setting/profile/{userId}")
    public ProfileResponse updateProfile(@PathVariable UUID userId, @RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(userId, request);
    }
}
