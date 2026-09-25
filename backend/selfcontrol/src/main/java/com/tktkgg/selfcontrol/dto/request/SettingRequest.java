package com.tktkgg.selfcontrol.dto.request;

import jakarta.validation.constraints.NotNull;

public record SettingRequest(
    @NotNull
    Boolean isPublic,

    @NotNull
    Boolean isAuthorizeNotification
) {}
