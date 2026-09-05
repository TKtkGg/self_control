package com.tktkgg.selfcontrol.dto.response;

import java.util.UUID;

public record ProfileResponse(
    UUID id, 
    String username, 
    byte[] icon, 
    String selfIntroduce, 
    int likeCount, 
    Boolean isLiked
) {}