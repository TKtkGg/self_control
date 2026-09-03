package com.tktkgg.selfcontrol.dto.response;

import java.util.UUID;

public class ProfileResponse {
    private UUID id;
    private String username;
    private byte[] icon;
    private String selfIntroduce;
    private int likeCount;
    private Boolean isLiked;

    public ProfileResponse(
        UUID id, 
        String username, 
        byte[] icon, 
        String selfIntroduce,
        int likeCount,
        Boolean isLiked
    ) {
        this.id = id;
        this.username = username;
        this.icon = icon;
        this.selfIntroduce = selfIntroduce;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
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

    public int getLikeCount() {
        return this.likeCount;
    }

    public Boolean getIsLiked() {
        return this.isLiked;
    }
}
