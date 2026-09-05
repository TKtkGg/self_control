package com.tktkgg.selfcontrol.dto.response;

import java.util.UUID;

public record UserResponse(
    UUID id, 
    String name
) {}

