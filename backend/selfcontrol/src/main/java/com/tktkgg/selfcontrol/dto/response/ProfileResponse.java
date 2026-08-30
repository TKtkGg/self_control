package com.tktkgg.selfcontrol.dto.response;

import java.util.UUID;

public class ProfileResponse {
    private UUID id;
    private String username;
    private byte[] icon;
    private String selfIntroduce;

    public ProfileResponse(UUID id, String username, byte[] icon, String selfIntroduce) {
        this.id = id;
        this.username = username;
        this.icon = icon;
        this.selfIntroduce = selfIntroduce;
    }

    public UUID getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }
    
    public byte[] getIcon() {
        return this.icon;
    }

    public String getSelfIntroduce() {
        return this.selfIntroduce;
    }
}
