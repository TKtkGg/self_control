package com.tktkgg.selfcontrol.dto.response;

import java.time.LocalDateTime;

public record AlarmResponse(
    TaskResponse task,
    LocalDateTime nextStartAt
) {}
