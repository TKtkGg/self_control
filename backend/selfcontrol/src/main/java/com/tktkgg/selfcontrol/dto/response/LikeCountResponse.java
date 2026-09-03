package com.tktkgg.selfcontrol.dto.response;

public class LikeCountResponse {
    private int likeCount;

    public LikeCountResponse(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getLikeCount() {
        return this.likeCount;
    }
}
