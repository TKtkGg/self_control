package com.tktkgg.selfcontrol.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.tktkgg.selfcontrol.dto.response.ProfileResponse;
import com.tktkgg.selfcontrol.dto.request.UpdateProfileRequest;
import com.tktkgg.selfcontrol.service.AuthService;
import com.tktkgg.selfcontrol.service.ProfileService;

@RestController
public class SettingController {
    private final ProfileService profileService;
    private final AuthService authService;

    public SettingController(ProfileService profileService, AuthService authService) {
        this.profileService = profileService;
        this.authService = authService;
    }

    @GetMapping("/api/setting/profile/{userId}")
    public ProfileResponse getProfile(@PathVariable UUID userId) {
        return profileService.getProfile(userId);
    }

    @GetMapping("/api/setting/profile")
    public ProfileResponse getProfile() {
        return profileService.getProfile(authService.getCurrentUserId());
    }

    @PatchMapping("/api/setting/profile")
    public ProfileResponse updateProfile(@RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(authService.getCurrentUserId(), request);
    }
}
