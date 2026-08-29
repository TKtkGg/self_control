package com.tktkgg.selfcontrol.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tktkgg.selfcontrol.dto.response.UsersResponse;
import com.tktkgg.selfcontrol.service.UserService;

@RestController
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public UsersResponse getUsers(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int size
    ) {
        return userService.getUsers(page, size);
    }
}
