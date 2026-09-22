package com.tktkgg.selfcontrol.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.tktkgg.selfcontrol.dto.request.SignUpRequest;
import com.tktkgg.selfcontrol.dto.request.LoginRequest;
import com.tktkgg.selfcontrol.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getUser() {
        boolean ok = authService.isAuthenticated();
        return ResponseEntity.ok(Map.of("authenticated", ok));
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUp(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request, HttpServletResponse response) {
        authService.signUp(signUpRequest.username(), signUpRequest.email(), signUpRequest.password(), signUpRequest.passwordConfirm(), request, response);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        authService.login(loginRequest.email(), loginRequest.password(), request, response);
        return ResponseEntity.ok(Map.of("message", "User logged in successfully"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(Map.of("message", "User logged out successfully"));
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
        return csrfToken;
    }
}
