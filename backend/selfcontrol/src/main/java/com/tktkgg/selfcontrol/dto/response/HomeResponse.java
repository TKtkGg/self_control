package com.tktkgg.selfcontrol.dto.response;

import java.util.List;

public class HomeResponse {
    List<DayScheduleResponse> daySchedules;

    public HomeResponse(List<DayScheduleResponse> daySchedules) {
        this.daySchedules = daySchedules;
    }

    public List<DayScheduleResponse> getDayScheduleResponses() {
        return daySchedules;
    }
}
