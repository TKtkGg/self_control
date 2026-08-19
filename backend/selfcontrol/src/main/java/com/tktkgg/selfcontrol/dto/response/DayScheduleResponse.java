package com.tktkgg.selfcontrol.dto.response;

import java.util.List;

public class DayScheduleResponse {
    private int dayOfWeek;
    private String title;
    private List<TaskResponse> tasks;

    public DayScheduleResponse(int dayOfWeek, String title, List<TaskResponse> tasks) {
        this.dayOfWeek = dayOfWeek;
        this.title = title;
        this.tasks = tasks;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public String getTitle() {
        return title;
    }

    public List<TaskResponse> getTasks() {
        return tasks;
    }
}
