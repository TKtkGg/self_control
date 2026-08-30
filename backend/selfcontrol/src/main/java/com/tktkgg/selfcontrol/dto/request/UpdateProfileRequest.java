package com.tktkgg.selfcontrol.dto.request;

public class UpdateProfileRequest {
    private String username;
    private byte[] icon;
    private String selfIntroduce;

    public UpdateProfileRequest(String username, byte[] icon, String selfIntroduce) {
        this.username = username;
        this.icon = icon;
        this.selfIntroduce = selfIntroduce;
    }

    public UpdateProfileRequest() {}
    
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
