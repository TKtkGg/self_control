package com.tktkgg.selfcontrol.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotBlank
    @Size(max = 20)
    String username, 

    @NotBlank
    @Email
    String email, 

    @NotBlank
    @Size(min = 8, max = 64)
    @Pattern(
        regexp = "(?s)(?=.*[A-Za-z])(?=.*[0-9]).*",
        message = "Password must contain at least one English letter and one digit."
    )
    String password, 

    @NotBlank
    String passwordConfirm
) {}
