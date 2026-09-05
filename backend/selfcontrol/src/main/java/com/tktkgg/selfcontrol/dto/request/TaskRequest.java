package com.tktkgg.selfcontrol.dto.request;

public record TaskRequest(
    int dayOfWeek, 
    int startHour, 
    int startMinute, 
    int endHour, 
    int endMinute, 
    String name
) {}