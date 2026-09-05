package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tktkgg.selfcontrol.dto.response.ProfileResponse;
import com.tktkgg.selfcontrol.dto.response.SettingResponse;
import com.tktkgg.selfcontrol.dto.request.UpdateProfileRequest;
import com.tktkgg.selfcontrol.dto.request.SettingRequest;
import com.tktkgg.selfcontrol.service.AuthService;
import com.tktkgg.selfcontrol.service.ProfileService;
import com.tktkgg.selfcontrol.service.SettingService;

@RestController
@RequestMapping("/api/setting")
public class SettingController {
    private final ProfileService profileService;
    private final SettingService settingService;
    private final AuthService authService;

    public SettingController(ProfileService profileService, SettingService settingService, AuthService authService) {
        this.profileService = profileService;
        this.authService = authService;
        this.settingService = settingService;
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile() {
        return profileService.getProfile(authService.getCurrentUserId());
    }

    @PatchMapping("/profile")
    public ProfileResponse updateProfile(@RequestBody UpdateProfileRequest request) {
        return profileService.updateProfile(authService.getCurrentUserId(), request);
    }

    @GetMapping("/setting")
    public SettingResponse getSetting() {
        return settingService.getSetting();
    }

    @PatchMapping("/setting")
    public SettingResponse updateSetting(@RequestBody SettingRequest request) {
        return settingService.updateSetting(request);
    }
}
