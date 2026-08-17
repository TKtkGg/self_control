package com.tktkgg.selfcontrol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tktkgg.selfcontrol.dto.request.SignUpRequest;
import com.tktkgg.selfcontrol.dto.request.LoginRequest;
import com.tktkgg.selfcontrol.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest, HttpServletRequest request, HttpServletResponse response) {
        authService.signUp(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getPasswordConfirm(), request, response);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        authService.login(loginRequest.getEmail(), loginRequest.getPassword(), request, response);
        return ResponseEntity.ok("User logged in successfully");
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok("User logged out successfully");
    }
}
