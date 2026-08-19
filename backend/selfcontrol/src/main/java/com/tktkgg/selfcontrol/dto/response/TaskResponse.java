package com.tktkgg.selfcontrol.dto.response;

import java.time.LocalTime;
import java.util.UUID;

public class TaskResponse {
    private UUID id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;

    public TaskResponse(UUID id, String name, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
}
