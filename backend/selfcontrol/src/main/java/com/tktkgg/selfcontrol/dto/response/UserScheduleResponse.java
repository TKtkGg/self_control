package com.tktkgg.selfcontrol.dto.response;

import java.util.List;

public record UserScheduleResponse(
    List<DayScheduleResponse> daySchedules
) {}