package com.tktkgg.selfcontrol.dto.response;

import java.util.List;

public record DayScheduleResponse(
    int dayOfWeek, 
    String title, 
    List<TaskResponse> tasks
) {}