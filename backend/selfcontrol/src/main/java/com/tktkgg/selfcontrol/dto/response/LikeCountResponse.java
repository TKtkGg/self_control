package com.tktkgg.selfcontrol.dto.response;

public class LikeCountResponse {
    private int likeCount;
    private Boolean isLiked;

    public LikeCountResponse(int likeCount, Boolean isLiked) {
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    public LikeCountResponse(int likeCount) {
        this.likeCount = likeCount;
        this.isLiked = null;
    }

    public int getLikeCount() {
        return this.likeCount;
    }

    public Boolean getIsLiked() {
        return this.isLiked;
    }
}
