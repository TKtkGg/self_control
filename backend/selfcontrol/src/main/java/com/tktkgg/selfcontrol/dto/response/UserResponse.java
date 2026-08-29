package com.tktkgg.selfcontrol.dto.response;

import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String name;

    public UserResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

