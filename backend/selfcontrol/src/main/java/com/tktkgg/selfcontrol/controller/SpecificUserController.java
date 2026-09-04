package com.tktkgg.selfcontrol.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tktkgg.selfcontrol.service.AuthService;
import com.tktkgg.selfcontrol.service.UserService;
import com.tktkgg.selfcontrol.service.ProfileService;
import com.tktkgg.selfcontrol.dto.response.ProfileResponse;
import com.tktkgg.selfcontrol.dto.response.LikeCountResponse;
import com.tktkgg.selfcontrol.service.UserScheduleService;
import com.tktkgg.selfcontrol.dto.response.UserScheduleResponse;

@RestController
public class SpecificUserController {
    private final AuthService authService;
    private final UserService userService;
    private final ProfileService profileService;
    private final UserScheduleService userScheduleService;

    public SpecificUserController(
        AuthService authService, 
        UserService userService,
        ProfileService profileService,
        UserScheduleService userScheduleService
    ) {
        this.authService = authService;
        this.userService = userService;
        this.profileService = profileService;
        this.userScheduleService = userScheduleService;
    }

    @GetMapping("/api/users/{userId}/profile")
    public ProfileResponse getProfile(@PathVariable UUID userId) {
        return profileService.getProfile(userId);
    }

    @GetMapping("/api/users/{userId}/schedule")
    public UserScheduleResponse getUserSchedule(@PathVariable UUID userId) {
        return userScheduleService.getUserSchedule(userId);
    }

    @PostMapping("/api/users/{userId}/like")
    public LikeCountResponse likeUser(@PathVariable UUID userId) {
        return userService.likeUser(authService.getCurrentUserId(), userId);
    }

    @DeleteMapping("/api/users/{userId}/like")
    public LikeCountResponse unlikeUser(@PathVariable UUID userId) {
        return userService.unlikeUser(authService.getCurrentUserId(), userId);
    }
}
