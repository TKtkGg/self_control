package com.tktkgg.selfcontrol.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @NotBlank
    @Size(max = 20)
    String username, 

    byte[] icon, 

    @Size(max = 500)
    String selfIntroduce
) {}
