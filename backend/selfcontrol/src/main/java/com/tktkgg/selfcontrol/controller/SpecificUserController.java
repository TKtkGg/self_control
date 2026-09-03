package com.tktkgg.selfcontrol.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tktkgg.selfcontrol.service.AuthService;
import com.tktkgg.selfcontrol.service.UserService;
import com.tktkgg.selfcontrol.service.ProfileService;
import com.tktkgg.selfcontrol.dto.response.UserResponse;
import com.tktkgg.selfcontrol.dto.response.ProfileResponse;

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
    public ResponseEntity<Map<String, String>> likeUser(@PathVariable UUID userId) {
        userService.likeUser(authService.getCurrentUserId(), userId);
        return ResponseEntity.ok(Map.of("message", "User liked successfully"));
    }
}
