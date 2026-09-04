package com.tktkgg.selfcontrol.dto.response;

import java.util.List;

public class UserScheduleResponse {
    List<DayScheduleResponse> daySchedules;

    public UserScheduleResponse(List<DayScheduleResponse> daySchedules) {
        this.daySchedules = daySchedules;
    }

    public List<DayScheduleResponse> getDayScheduleResponses() {
        return daySchedules;
    }
}
