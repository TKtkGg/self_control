package com.tktkgg.selfcontrol.dto.response;

public record SettingResponse(
    boolean isPublic,
    boolean isAuthorizeNotification
) {}
