package com.tktkgg.selfcontrol.dto.request;

public record SignUpRequest(
    String username, 
    String email, 
    String password, 
    String passwordConfirm
) {}
