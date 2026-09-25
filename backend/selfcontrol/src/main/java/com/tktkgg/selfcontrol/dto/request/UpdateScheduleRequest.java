package com.tktkgg.selfcontrol.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateScheduleRequest(
    @NotNull
    @Size(max = 30)
    String title
) {}
