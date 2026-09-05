package com.tktkgg.selfcontrol.dto.request;

public record UpdateProfileRequest(
    String username, 
    byte[] icon, 
    String selfIntroduce
) {}
