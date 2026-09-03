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

@RestController
public class SpecificUserController {
    private final AuthService authService;
    private final UserService userService;
    private final ProfileService profileService;

    public SpecificUserController(
        AuthService authService, 
        UserService userService,
        ProfileService profileService
    ) {
        this.authService = authService;
        this.userService = userService;
        this.profileService = profileService;
    }

    @GetMapping("/api/users/{userId}/profile")
    public ProfileResponse getProfile(@PathVariable UUID userId) {
        return profileService.getProfile(userId);
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
