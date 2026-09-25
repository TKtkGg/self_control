package com.tktkgg.selfcontrol.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskRequest(
    @NotNull
    @Min(0)
    @Max(6)
    Integer dayOfWeek, 

    @NotNull
    @Min(0)
    @Max(23)
    Integer startHour, 

    @NotNull
    @Min(0)
    @Max(59)
    Integer startMinute, 

    @NotNull
    @Min(0)
    @Max(23)
    Integer endHour, 

    @NotNull
    @Min(0)
    @Max(59)
    Integer endMinute, 

    @NotBlank
    @Size(max = 30)
    String name
) {}
