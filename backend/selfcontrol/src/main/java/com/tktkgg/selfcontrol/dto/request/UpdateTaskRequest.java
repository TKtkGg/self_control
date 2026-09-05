package com.tktkgg.selfcontrol.dto.request;

public record UpdateTaskRequest(
    int startHour, 
    int startMinute, 
    int endHour, 
    int endMinute, 
    String name
) {}