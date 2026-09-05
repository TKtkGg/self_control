package com.tktkgg.selfcontrol.dto.response;

import java.time.LocalTime;
import java.util.UUID;

public record TaskResponse(
    UUID id, 
    String name, 
    LocalTime startTime, 
    LocalTime endTime
) {}

