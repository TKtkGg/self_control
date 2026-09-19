package com.tktkgg.selfcontrol.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotBlank
    @Size(max = 20)
    String username, 

    @NotBlank
    @Email
    String email, 

    @NotBlank
    String password, 

    @NotBlank
    String passwordConfirm
) {}
