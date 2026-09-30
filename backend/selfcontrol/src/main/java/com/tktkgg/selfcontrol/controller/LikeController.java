package com.tktkgg.selfcontrol.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tktkgg.selfcontrol.service.auth.AuthService;
import com.tktkgg.selfcontrol.service.user.UserService;
import com.tktkgg.selfcontrol.dto.response.LikeCountResponse;

@RestController
@RequestMapping("/api/users")
public class LikeController {
    private final AuthService authService;
    private final UserService userService;

    public LikeController(
        AuthService authService, 
        UserService userService
    ) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/{userId}/like")
    public LikeCountResponse likeUser(@PathVariable UUID userId) {
        return userService.likeUser(authService.getCurrentUserId(), userId);
    }

    @DeleteMapping("/{userId}/like")
    public LikeCountResponse unlikeUser(@PathVariable UUID userId) {
        return userService.unlikeUser(authService.getCurrentUserId(), userId);
    }
}
