package com.tktkgg.selfcontrol.dto.request;

public record SettingRequest(
    boolean isPublic,
    boolean isAuthorizeNotification
) {}
